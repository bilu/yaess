package pl.biltec.loyalyty.domain.customer.event;

import java.time.LocalDateTime;

import pl.biltec.loyalyty.domain.customer.CustomerId;


public class CustomerDeletedEvent extends CustomerEvent {


	public CustomerDeletedEvent(CustomerId customerId, LocalDateTime timestamp) {

		super(customerId, timestamp);
	}

	@Override
	public String toString() {

		return "CustomerDeletedEvent{" +
			"customerId=" + customerId +
			", created=" + created +
			'}';
	}
}
