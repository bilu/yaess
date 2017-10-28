package pl.biltec.yaess.clp.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.biltec.yaess.clp.domain.event.CustomerRenamedEvent;
import pl.biltec.yaess.core.adapters.store.EventSubscriber;
import pl.biltec.yaess.core.domain.Event;


public class CustomerRenamedEventLogger implements EventSubscriber {

	private static final Logger logger = LoggerFactory.getLogger(CustomerRenamedEventLogger.class);

	@Override
	public void handleEvent(Event event) {

		CustomerRenamedEvent customerRenamedEvent = (CustomerRenamedEvent) event;
		logger.info(this.getClass().getSimpleName() + ": " + customerRenamedEvent);
	}

	@Override
	public boolean supports(Event event) {

		return event.getClass().isInstance(CustomerRenamedEvent.class);
	}

}
