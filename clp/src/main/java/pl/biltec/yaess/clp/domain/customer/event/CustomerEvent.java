package pl.biltec.yaess.clp.domain.customer.event;

import java.time.LocalDateTime;

import pl.biltec.yaess.clp.domain.customer.CustomerId;
import pl.biltec.yaess.core.domain.Event;


public abstract class CustomerEvent extends Event<CustomerId> {

	protected CustomerEvent(CustomerId id, LocalDateTime created) {

		super(id, created);
	}
}
