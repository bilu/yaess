package pl.biltec.loyalyty.domain.customer;

import java.util.List;

import pl.biltec.loyalyty.application.customer.CustomerEventSubscriber;
import pl.biltec.loyalyty.application.customer.CustomerEventSubscriberExtended;
import pl.biltec.loyalyty.domain.customer.event.CustomerEvent;


public interface CustomerEventStore {

	CustomerEventsStream loadEvents(CustomerId customerId);
	CustomerEventsStream loadEvents(CustomerId customerId, int skipEvents, int maxCount);

	void appendEvents(CustomerId customerId, List<CustomerEvent> events, int expectedConcurrencyVersion);

	void addEventSubscriber(CustomerEventSubscriber<? extends CustomerEvent> eventSubscriber);
	void addEventSubscriber(CustomerEventSubscriberExtended eventSubscriberExtended);

	boolean alreadyExists(CustomerId customerId);
}

