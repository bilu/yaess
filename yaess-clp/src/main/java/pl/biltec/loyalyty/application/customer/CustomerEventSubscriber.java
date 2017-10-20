package pl.biltec.loyalyty.application.customer;

import pl.biltec.loyalyty.domain.customer.event.CustomerEvent;


public interface CustomerEventSubscriber<EVENT extends CustomerEvent> {

	void handleEvent(final EVENT event);

	Class<EVENT> eventType();
}
