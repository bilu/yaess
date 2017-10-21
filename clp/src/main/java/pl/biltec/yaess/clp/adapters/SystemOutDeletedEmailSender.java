package pl.biltec.yaess.clp.adapters;

import pl.biltec.yaess.clp.domain.customer.CustomerId;
import pl.biltec.yaess.clp.ports.customer.EventSubscriber;
import pl.biltec.yaess.clp.domain.customer.event.CustomerDeletedEvent;


public class SystemOutDeletedEmailSender implements EventSubscriber<CustomerId, CustomerDeletedEvent> {

	@Override
	public void handleEvent(CustomerDeletedEvent customerRenamedEvent) {

		System.out.println(this.getClass().getSimpleName() + ": " + customerRenamedEvent);
	}

	@Override
	public Class<CustomerDeletedEvent> eventType() {
		// TODO: [pbilewic] 11.10.17 da sie ograc automatem z generyków (pryznajmniej w springu)
		return CustomerDeletedEvent.class;
	}
}
