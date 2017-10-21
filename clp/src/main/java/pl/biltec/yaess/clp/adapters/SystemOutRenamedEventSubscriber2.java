package pl.biltec.yaess.clp.adapters;

import pl.biltec.yaess.clp.domain.customer.CustomerId;
import pl.biltec.yaess.clp.domain.customer.event.CustomerRenamedEvent;
import pl.biltec.yaess.core.adapters.store.EventSubscriber;


public class SystemOutRenamedEventSubscriber2 implements EventSubscriber<CustomerId, CustomerRenamedEvent> {

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
