package pl.biltec.yaess.clp.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.biltec.yaess.clp.domain.event.CustomerDeletedEvent;
import pl.biltec.yaess.core.adapters.store.EventSubscriber;
import pl.biltec.yaess.core.domain.Event;


public class CustomerDeletedEmailSender implements EventSubscriber {

	private static final Logger logger = LoggerFactory.getLogger(CustomerDeletedEmailSender.class);

	@Override
	public void handleEvent(Event event) {

		CustomerDeletedEvent customerDeletedEvent = (CustomerDeletedEvent) event;
		logger.info(this.getClass().getSimpleName() + ": " + customerDeletedEvent);
	}

	@Override
	public boolean supports(Event event) {

		return event.getClass().isInstance(CustomerDeletedEvent.class);
	}
}
