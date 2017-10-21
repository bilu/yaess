package pl.biltec.yaess.clp.adapters;

import pl.biltec.yaess.clp.domain.customer.CustomerId;
import pl.biltec.yaess.clp.domain.customer.event.CustomerEvent;
import pl.biltec.yaess.clp.ports.customer.ExtendedEventSubscriber;


public class SystemOutAllEventsEventSubscriberExtendedEventSubscriber implements ExtendedEventSubscriber<CustomerId, CustomerEvent> {

	@Override
	public void handleEvent(CustomerEvent event) {

		System.out.println(this.getClass().getSimpleName() + ": " + event);

	}

	@Override
	public boolean supports(CustomerEvent event) {

		return true;
	}
}
