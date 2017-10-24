package pl.biltec.yaess.clp.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.biltec.yaess.core.adapters.store.ExtendedEventSubscriber;
import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class AllCustomerEventsLogger implements ExtendedEventSubscriber<RootAggregateId, Event> {

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
