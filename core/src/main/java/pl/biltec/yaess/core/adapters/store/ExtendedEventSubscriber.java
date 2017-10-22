package pl.biltec.yaess.core.adapters.store;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public interface ExtendedEventSubscriber<ID extends RootAggregateId, EVENT extends Event<ID>> {

	void handleEvent(final EVENT event);

	boolean supports(final EVENT event);
}
