package pl.biltec.yaess.core.adapters.store;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public interface EventSubscriber<ID extends RootAggregateId, EVENT extends Event<ID>> {

	void handleEvent(final EVENT event);

	Class<EVENT> eventType();
}
