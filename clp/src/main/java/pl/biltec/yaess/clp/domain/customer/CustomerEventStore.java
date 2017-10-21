package pl.biltec.yaess.clp.domain.customer;

import java.util.List;

import pl.biltec.yaess.clp.domain.customer.event.CustomerEvent;
import pl.biltec.yaess.clp.ports.customer.CustomerEventSubscriber;
import pl.biltec.yaess.clp.ports.customer.CustomerEventSubscriberExtended;


public interface CustomerEventStore {

	List<CustomerEvent> loadEvents(CustomerId customerId);
	List<CustomerEvent> loadEvents(CustomerId customerId, int skipEvents, int maxCount);

	void appendEvents(CustomerId customerId, List<CustomerEvent> events, long expectedConcurrencyVersion);

	void addEventSubscriber(CustomerEventSubscriber<? extends CustomerEvent> eventSubscriber);
	void addEventSubscriber(CustomerEventSubscriberExtended eventSubscriberExtended);

	boolean exists(CustomerId customerId);
}

