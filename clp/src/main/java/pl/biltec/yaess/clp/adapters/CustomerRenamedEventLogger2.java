package pl.biltec.yaess.clp.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.biltec.yaess.clp.domain.customer.CustomerId;
import pl.biltec.yaess.clp.domain.customer.event.CustomerRenamedEvent;
import pl.biltec.yaess.core.adapters.store.EventSubscriber;


public class CustomerRenamedEventLogger2 implements EventSubscriber<CustomerId, CustomerRenamedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(CustomerRenamedEventLogger2.class);

	@Override
	public void handleEvent(CustomerRenamedEvent customerRenamedEvent) {

		logger.info(this.getClass().getSimpleName() + ": " + customerRenamedEvent);
	}

	@Override
	public Class<CustomerRenamedEvent> eventType() {
		// TODO: [pbilewic] 11.10.17 da sie ograc automatem z generyków (pryznajmniej w springu)
		return CustomerRenamedEvent.class;
	}
}
