
package pl.biltec.yaess.core.adapters.store.memory;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;

import pl.biltec.yaess.core.adapters.store.EventStore;
import pl.biltec.yaess.core.adapters.store.EventSubscriber;
import pl.biltec.yaess.core.common.Contract;
import pl.biltec.yaess.core.common.Timer;
import pl.biltec.yaess.core.common.exception.ConcurrentModificationException;
import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class InMemoryEventStore implements EventStore {

	private Gson gson = new Gson();
	private List<EventRecord> orderedEvents = Collections.synchronizedList(new LinkedList<>());
	// TODO [bilu] 25.10.17 subscribers and publishing should not be managed by EventStore
	private Set<EventSubscriber> subscribers = Collections.synchronizedSet(new HashSet<>());
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
			//			long calculatedConcurrencyVersion = eventStream(id, rootAggregateClass).count();
			long calculatedConcurrencyVersion = Timer.count("eventStream(id, rootAggregateClass).count()", () -> eventStream(id, rootAggregateClass).count());
			long expectedConcurrencyVersion = currentConcurrencyVersion - events.size();
			if (calculatedConcurrencyVersion != expectedConcurrencyVersion) {
				throw new ConcurrentModificationException(id.toString(), expectedConcurrencyVersion, calculatedConcurrencyVersion);
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
		//sync
		//		publish(toBePublished);
	}

	private EventRecord convertToEventRecord(Event newEvent, Class<? extends RootAggregate> rootAggregateClass) {

		return new EventRecord(
			rootAggregateClass.getSimpleName(),
			newEvent.rootAggregateId().toString(),
			newEvent.getClass().getName(),
			gson.toJson(newEvent),
			newEvent.created());
	}

	private void publish(List<Event> toBePublished) {

		toBePublished.stream()
			.parallel()
			.forEach(
				event -> {
					subscribers.stream()
						.filter(extendedEventSubscriber -> extendedEventSubscriber.supports(event))
						.forEach(subscriberExtended -> subscriberExtended.handleEvent(event));
				}
			);
	}

	@Override
	public void addEventSubscriber(EventSubscriber eventSubscriber) {

		subscribers.add(eventSubscriber);
	}
}
