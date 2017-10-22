package pl.biltec.yaess.clp.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.biltec.yaess.clp.domain.customer.CustomerId;
import pl.biltec.yaess.clp.domain.customer.event.CustomerEvent;
import pl.biltec.yaess.core.adapters.store.ExtendedEventSubscriber;


public class AllCustomerEventsLogger implements ExtendedEventSubscriber<CustomerId, CustomerEvent> {

	private static final Logger logger = LoggerFactory.getLogger(AllCustomerEventsLogger.class);

	@Override
	public void handleEvent(CustomerEvent event) {

		logger.info(this.getClass().getSimpleName() + ": " + event);

	}

	@Override
	public boolean supports(CustomerEvent event) {

		return true;
	}
}
