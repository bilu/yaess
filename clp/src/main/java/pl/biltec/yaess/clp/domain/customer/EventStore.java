package pl.biltec.yaess.clp.domain.customer;

import java.util.List;

import pl.biltec.yaess.clp.ports.customer.EventSubscriber;
import pl.biltec.yaess.clp.ports.customer.ExtendedEventSubscriber;
import pl.biltec.yaess.core.domain.AbstractEvent;
import pl.biltec.yaess.core.domain.AggregateId;


public interface EventStore<ID extends AggregateId, EVENT extends AbstractEvent<ID>> {

	boolean exists(ID id);
	List<EVENT> loadEvents(ID id);
	List<EVENT> loadEvents(ID id, int skipEvents, int maxCount);
	void appendEvents(ID id, List<EVENT> events, long expectedConcurrencyVersion);

	void addEventSubscriber(EventSubscriber<ID, EVENT> eventSubscriber);
	void addEventSubscriber(ExtendedEventSubscriber eventSubscriberExtended);

}

