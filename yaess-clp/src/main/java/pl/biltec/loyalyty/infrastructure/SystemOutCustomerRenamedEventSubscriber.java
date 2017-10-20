package pl.biltec.loyalyty.infrastructure;

import pl.biltec.loyalyty.application.customer.CustomerEventSubscriber;
import pl.biltec.loyalyty.domain.customer.event.CustomerRenamedEvent;


public class SystemOutCustomerRenamedEventSubscriber implements CustomerEventSubscriber<CustomerRenamedEvent> {

	@Override
	public void handleEvent(CustomerRenamedEvent customerRenamedEvent) {

		System.out.println(this.getClass().getSimpleName() + ": " + customerRenamedEvent);
	}

	@Override
	public Class<CustomerRenamedEvent> eventType() {
		// TODO: [pbilewic] 11.10.17 da sie ograc automatem z generyków (pryznajmniej w springu)
		return CustomerRenamedEvent.class;
	}
}
