package pl.biltec.yaess.core.adapters.store;

import pl.biltec.yaess.core.domain.AbstractEvent;
import pl.biltec.yaess.core.domain.AggregateId;


public interface EventSubscriber<ID extends AggregateId, EVENT extends AbstractEvent<ID>> {

	void handleEvent(final EVENT event);

	Class<EVENT> eventType();
}
