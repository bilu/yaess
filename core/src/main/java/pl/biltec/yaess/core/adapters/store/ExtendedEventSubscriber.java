package pl.biltec.yaess.core.adapters.store;

import pl.biltec.yaess.core.domain.AbstractEvent;
import pl.biltec.yaess.core.domain.AggregateId;


public interface ExtendedEventSubscriber<ID extends AggregateId, EVENT extends AbstractEvent<ID>> {

	void handleEvent(final EVENT event);

	boolean supports(final EVENT event);
}
