package pl.biltec.yaess.clp.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.biltec.yaess.core.adapters.store.EventSubscriber;
import pl.biltec.yaess.core.domain.Event;


public class AllCustomerEventsLogger implements EventSubscriber {

	private static final Logger logger = LoggerFactory.getLogger(AllCustomerEventsLogger.class);

	@Override
	public void handleEvent(Event event) {

		logger.info(this.getClass().getSimpleName() + ": " + event);

	}

	@Override
	public boolean supports(Event event) {

		return event.getClass().getSimpleName().contains("Customer");
	}
}
