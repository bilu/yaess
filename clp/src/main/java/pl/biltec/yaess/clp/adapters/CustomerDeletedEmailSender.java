package pl.biltec.yaess.clp.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.biltec.yaess.clp.event.CustomerDeletedEvent;
import pl.biltec.yaess.core.adapters.store.EventSubscriber;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerDeletedEmailSender implements EventSubscriber<RootAggregateId, CustomerDeletedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(CustomerDeletedEmailSender.class);
	@Override
	public void handleEvent(CustomerDeletedEvent customerRenamedEvent) {

		logger.info(this.getClass().getSimpleName() + ": " + customerRenamedEvent);
	}

	@Override
	public Class<CustomerDeletedEvent> eventType() {
		// TODO: [pbilewic] 11.10.17 da sie ograc automatem z generyków (pryznajmniej w springu)
		return CustomerDeletedEvent.class;
	}
}
