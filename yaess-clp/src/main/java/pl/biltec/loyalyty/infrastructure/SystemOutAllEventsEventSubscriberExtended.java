package pl.biltec.loyalyty.infrastructure;

import pl.biltec.loyalyty.application.customer.CustomerEventSubscriberExtended;
import pl.biltec.loyalyty.domain.customer.event.CustomerEvent;


public class SystemOutAllEventsEventSubscriberExtended implements CustomerEventSubscriberExtended {

	@Override
	public void handleEvent(CustomerEvent event) {
		System.out.println(this.getClass().getSimpleName() + ": " + event);

	}

	@Override
	public boolean supports(CustomerEvent event) {

		return true;
	}
}
