package pl.biltec.yaess.core.adapters.store;

import java.util.List;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


public interface EventStore {

	boolean exists(RootAggregateId id, Class<? extends RootAggregate> rootAggregateClass);

	List<Event> loadEvents(RootAggregateId id, Class<? extends RootAggregate> rootAggregateClass);

	List<Event> loadEvents(RootAggregateId id, Class<? extends RootAggregate> rootAggregateClass, long skipEvents, long maxCount);

	void appendEvents(RootAggregateId id, Class<? extends RootAggregate> rootAggregateClass, List<Event> events, long currentConcurrencyVersion);


	// TODO [bilu] 25.10.17 subscribers and publishing should not be managed by EventStore
	void addEventSubscriber(EventSubscriber eventSubscriber);
}
