package pl.biltec.loyalyty.application.customer;

import pl.biltec.loyalyty.domain.customer.event.CustomerEvent;


public interface CustomerEventSubscriberExtended {

	void handleEvent(final CustomerEvent event);

	boolean supports(CustomerEvent event);
}
