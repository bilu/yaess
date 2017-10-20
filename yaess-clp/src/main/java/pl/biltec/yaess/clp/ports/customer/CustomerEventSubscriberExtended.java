package pl.biltec.yaess.clp.ports.customer;

import pl.biltec.yaess.clp.domain.customer.event.CustomerEvent;


public interface CustomerEventSubscriberExtended {

	void handleEvent(final CustomerEvent event);

	boolean supports(CustomerEvent event);
}
