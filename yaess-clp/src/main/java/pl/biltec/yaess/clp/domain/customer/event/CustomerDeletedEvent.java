package pl.biltec.yaess.clp.domain.customer.event;

import java.time.LocalDateTime;

import pl.biltec.yaess.clp.domain.customer.CustomerId;


public class CustomerDeletedEvent extends CustomerEvent {


	public CustomerDeletedEvent(CustomerId id, LocalDateTime timestamp) {

		super(id, timestamp);
	}

	@Override
	public String toString() {

		return "CustomerDeletedEvent{" +
			"id=" + id +
			", version=" + version +
			", created=" + created +
			'}';
	}
}
