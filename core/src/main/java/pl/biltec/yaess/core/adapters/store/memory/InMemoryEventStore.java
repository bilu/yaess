
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

import com.google.gson.Gson;

import pl.biltec.yaess.core.adapters.store.EventStore;
import pl.biltec.yaess.core.adapters.store.EventSubscriber;
import pl.biltec.yaess.core.adapters.store.ExtendedEventSubscriber;
import pl.biltec.yaess.core.common.Contract;
import pl.biltec.yaess.core.common.exception.ConcurrentModificationException;
import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class InMemoryEventStore implements EventStore {

	private Gson gson = new Gson();
	private List<EventRecord> orderedEvents = Collections.synchronizedList(new LinkedList<>());
	// TODO [bilu] 25.10.17 subscribers and publishing should not be managed by EventStore
	private Map<Class<Event>, List<Object>> subscribers = Collections.synchronizedMap(new HashMap<>());
	private Set<ExtendedEventSubscriber> subscribersExtended = Collections.synchronizedSet(new HashSet<>());
	ExecutorService executor = Executors.newSingleThreadExecutor();

	@Override
	public boolean exists(RootAggregateId id, Class<? extends RootAggregate> rootAggregateClass) {

		Contract.notNull(id, "id");
		Contract.notNull(rootAggregateClass, "rootAggregateClass");

		return eventStream(id, rootAggregateClass)
			.findFirst()
			.isPresent();
	}

	private Stream<Event> eventStream(RootAggregateId id, Class<? extends RootAggregate> rootAggregateClass) {

		return orderedEvents.stream()
			.filter(event -> event.getRootAggregateName().equals(rootAggregateClass.getSimpleName().toString()))
			.filter(event -> event.getRootId().equals(id.toString()))
			.map(event -> {
				try {
					return (Event) gson.fromJson(event.getEventAsJson(), Class.forName(event.getEventClassName()));
				}
				catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			});
	}

	@Override
	public List<Event> loadEvents(RootAggregateId id, Class<? extends RootAggregate> rootAggregateClass) {

		Contract.notNull(id, "id");
		Contract.notNull(rootAggregateClass, "rootAggregateClass");

		return eventStream(id, rootAggregateClass)
			.collect(Collectors.toList());
	}

	@Override
	public List<Event> loadEvents(RootAggregateId id, Class<? extends RootAggregate> rootAggregateClass, long skipEvents, long maxCount) {

		return eventStream(id, rootAggregateClass)
			.skip(skipEvents)
			.limit(maxCount)
			.collect(Collectors.toList());
	}

	@Override
	public synchronized void appendEvents(RootAggregateId id, Class<? extends RootAggregate> rootAggregateClass, List<Event> events, long currentConcurrencyVersion) {

		Contract.notNull(id, "id");
		Contract.notNull(rootAggregateClass, "rootAggregateName");
		Contract.notNull(events, "events");
		Contract.notNull(currentConcurrencyVersion, "currentConcurrencyVersion");

		if (exists(id, rootAggregateClass)) {
			long calculatedConcurrencyVersion = eventStream(id, rootAggregateClass).count();

			if (calculatedConcurrencyVersion != (currentConcurrencyVersion - events.size())) {
				throw new ConcurrentModificationException(id.toString(), currentConcurrencyVersion, calculatedConcurrencyVersion);
			}
		}

		List<Event> toBePublished = new LinkedList<>();

		events.forEach(
			newEvent -> {
				orderedEvents.add(convertToEventRecord(newEvent, rootAggregateClass));
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

	private EventRecord convertToEventRecord(Event newEvent, Class<? extends RootAggregate> rootAggregateClass) {

		return new EventRecord(
			rootAggregateClass.getSimpleName(),
			newEvent.rootAggregateId().toString(),
			newEvent.getClass().getName(),
			newEvent.version(),
			gson.toJson(newEvent),
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
