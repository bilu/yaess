package pl.biltec.yaess.core.adapters.store;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import pl.biltec.yaess.core.common.Contract;
import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class UpcastingEventStoreWrapper implements EventStore {

	private EventStore wrappedEventStore;
	private DeprecatedEventsUpcaster upcaster;

	List<Event> upcast(List<Event> events) {

		return events.stream()
			.map(event -> {
				Class<? extends Event> eventClass = event.getClass();
				if (upcaster.contains(eventClass)) {
					return upcaster.upcast(eventClass, event);
				}
				return Arrays.asList(event);
			})
			.flatMap(List::stream)
			.collect(Collectors.toList());
	}

	public static void main(String[] args) {

		List<String> l = Arrays.asList("a", "b", "c");
		System.out.println(l);

		List<String> result = l.stream()
			.map(element -> Arrays.asList(element + 1, element + 2))
			.flatMap(List::stream)
			.collect(Collectors.toList());

		System.out.println(result);
	}

	public UpcastingEventStoreWrapper(EventStore wrappedEventStore, DeprecatedEventsUpcaster upcaster) {

		this.wrappedEventStore = Contract.notNull(wrappedEventStore, "wrappedEventStore");
		this.upcaster = Contract.notNull(upcaster, "upcaster");

	}

	@Override
	public boolean exists(RootAggregateId id, Class<? extends RootAggregate> rootAggregateClass) {

		return wrappedEventStore.exists(id, rootAggregateClass);
	}

	@Override
	public List<Event> loadEvents(RootAggregateId id, Class<? extends RootAggregate> rootAggregateClass) {

		return upcast(wrappedEventStore.loadEvents(id, rootAggregateClass));
	}

	@Override
	public List<Event> loadEvents(RootAggregateId id, Class<? extends RootAggregate> rootAggregateClass, long skipEvents, long maxCount) {

		return upcast(wrappedEventStore.loadEvents(id, rootAggregateClass, skipEvents, maxCount));
	}

	@Override
	public void appendEvents(RootAggregateId id, Class<? extends RootAggregate> rootAggregateClass, List<Event> events, long currentConcurrencyVersion) {

		wrappedEventStore.appendEvents(id, rootAggregateClass, events, currentConcurrencyVersion);
	}

	@Override
	public void addEventSubscriber(EventSubscriber eventSubscriber) {

		wrappedEventStore.addEventSubscriber(eventSubscriber);
	}

}
