package pl.biltec.yaess.clp.ports.customer;

import pl.biltec.yaess.clp.domain.customer.event.CustomerEvent;


public interface CustomerEventSubscriber<EVENT extends CustomerEvent> {

	void handleEvent(final EVENT event);

	Class<EVENT> eventType();
}
