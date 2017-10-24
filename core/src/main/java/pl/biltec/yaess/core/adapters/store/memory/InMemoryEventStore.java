
package pl.biltec.yaess.core.adapters.store.memory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.biltec.yaess.core.adapters.store.EventSubscriber;
import pl.biltec.yaess.core.adapters.store.ExtendedEventSubscriber;
import pl.biltec.yaess.core.common.Contract;
import pl.biltec.yaess.core.common.exception.ConcurrentModificationException;
import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class InMemoryEventStore {

	private List<EventRecord> orderedEvents = Collections.synchronizedList(new LinkedList<>());
	private Map<Class<Event>, List<Object>> subscribers = Collections.synchronizedMap(new HashMap<>());
	private Set<ExtendedEventSubscriber> subscribersExtended = Collections.synchronizedSet(new HashSet<>());
	ExecutorService executor = Executors.newSingleThreadExecutor();

	public boolean exists(String rootAggregateId, String rootAggregateName) {

		Contract.notNull(rootAggregateId, "rootAggregateId");
		Contract.notNull(rootAggregateName, "rootAggregateName");

		return eventStream(rootAggregateId, rootAggregateName)
			.findFirst()
			.isPresent();
	}

	private Stream<? extends Event> eventStream(String rootAggregateId, String rootAggregateName) {

		return orderedEvents.stream()
			.filter(event -> event.getRootAggregateName().equals(rootAggregateName))
			.filter(event -> event.getRootId().equals(rootAggregateId))
			.map(event -> (Event) event.getSerializedEvent());
	}

	public List<? extends Event> loadEvents(String rootAggregateId, String rootAggregateName) {

		Contract.notNull(rootAggregateId, "rootAggregateId");
		Contract.notNull(rootAggregateName, "rootAggregateName");

		return eventStream(rootAggregateId, rootAggregateName)
			.collect(Collectors.toList());
	}

	public List<? extends Event> loadEvents(String rootAggregateId, String rootAggregateName, int skipEvents, int maxCount) {

		return eventStream(rootAggregateId, rootAggregateName)
			.skip(skipEvents)
			.limit(maxCount)
			.collect(Collectors.toList());
	}

	public synchronized void appendEvents(String rootAggregateId, String rootAggregateName, List<? extends Event> events, long currentConcurrencyVersion) {

		Contract.notNull(rootAggregateId, "rootAggregateId");
		Contract.notNull(rootAggregateName, "rootAggregateName");
		Contract.notNull(events, "events");
		Contract.notNull(currentConcurrencyVersion, "currentConcurrencyVersion");

		if (exists(rootAggregateId, rootAggregateName)) {
			long calculatedConcurrencyVersion = eventStream(rootAggregateId, rootAggregateName).count();

			if (calculatedConcurrencyVersion != (currentConcurrencyVersion - events.size())) {
				throw new ConcurrentModificationException(rootAggregateId, currentConcurrencyVersion, calculatedConcurrencyVersion);
			}
		}

		List<Event> toBePublished = new LinkedList<>();

		events.forEach(
			newEvent -> {
				orderedEvents.add(convertToEventRecord(newEvent, rootAggregateName));
				toBePublished.add(newEvent);
			}
		);

		//async
		executor.submit(() -> publish(toBePublished));
		executor.submit(() -> publishExtended(toBePublished));
		//sync
		//		publish(toBePublished);
		//		publishExtended(toBePublished);
	}

	private EventRecord convertToEventRecord(Event newEvent, String rootAggregateName) {

		return new EventRecord(
			rootAggregateName,
			newEvent.rootAggregateId().toString(),
			newEvent.getClass().getSimpleName(),
			newEvent.version(),
			newEvent,
			newEvent.created());
	}

	private void publish(List<Event> toBePublished) {

		toBePublished.stream()
			.parallel()
			.forEach(
				event -> {
					List<Object> customerEventSubscribers = subscribers.get(event.getClass());
					if (customerEventSubscribers != null) {
						customerEventSubscribers.forEach(subscriber -> ((EventSubscriber) subscriber).handleEvent(event));
					}
				}
			);
	}

	private void publishExtended(List<Event> toBePublished) {

		toBePublished.stream()
			.parallel()
			.forEach(
				event -> {
					subscribersExtended.stream()
						.filter(extendedEventSubscriber -> extendedEventSubscriber.supports(event))
						.forEach(subscriberExtended -> subscriberExtended.handleEvent(event));
				}
			);
	}

	public void addEventSubscriber(EventSubscriber<RootAggregateId, Event> eventSubscriber) {

		// TODO: [pbilewic] 11.10.17 debilne, refaktor
		List<Object> customerEventSubscribers = subscribers.get(eventSubscriber.eventType());
		if (customerEventSubscribers == null) {
			customerEventSubscribers = Arrays.asList(eventSubscriber);
			subscribers.put(eventSubscriber.eventType(), customerEventSubscribers);
		}
		else {
			customerEventSubscribers = new ArrayList<>(customerEventSubscribers);
			customerEventSubscribers.add(eventSubscriber);
			subscribers.put(eventSubscriber.eventType(), customerEventSubscribers);
		}

	}

}