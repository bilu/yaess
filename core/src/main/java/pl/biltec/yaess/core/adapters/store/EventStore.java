package pl.biltec.yaess.core.adapters.store;

import java.util.List;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public interface EventStore<ID extends RootAggregateId, EVENT extends Event<ID>> {

	boolean exists(ID id);
	List<EVENT> loadEvents(ID id);
	List<EVENT> loadEvents(ID id, int skipEvents, int maxCount);
	void appendEvents(ID id, List<EVENT> events, long expectedConcurrencyVersion);

	void addEventSubscriber(EventSubscriber<ID, EVENT> eventSubscriber);
	void addEventSubscriber(ExtendedEventSubscriber eventSubscriberExtended);

}

