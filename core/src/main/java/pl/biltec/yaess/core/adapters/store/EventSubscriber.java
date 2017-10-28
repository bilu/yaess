package pl.biltec.yaess.core.adapters.store;

import pl.biltec.yaess.core.domain.Event;


public interface EventSubscriber {

	void handleEvent(final Event event);

	boolean supports(final Event event);
}
