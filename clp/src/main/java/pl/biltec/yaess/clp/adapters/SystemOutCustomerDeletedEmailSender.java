package pl.biltec.yaess.clp.adapters;

import pl.biltec.yaess.clp.ports.customer.CustomerEventSubscriber;
import pl.biltec.yaess.clp.domain.customer.event.CustomerDeletedEvent;


public class SystemOutCustomerDeletedEmailSender implements CustomerEventSubscriber<CustomerDeletedEvent> {

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
