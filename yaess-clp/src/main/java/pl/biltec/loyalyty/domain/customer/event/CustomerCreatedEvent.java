package pl.biltec.loyalyty.domain.customer.event;

import java.time.LocalDateTime;

import pl.biltec.loyalyty.domain.customer.CustomerId;


public class CustomerCreatedEvent extends CustomerEvent {

	private String name;

	public CustomerCreatedEvent(CustomerId customerId, String name, LocalDateTime created) {

		super(customerId, created);
		this.name = name;
	}

	public String getName() {

		return name;
	}

	@Override
	public String toString() {

		return "CustomerCreatedEvent{" +
			"newName='" + name + '\'' +
			", customerId=" + customerId +
			", created=" + created +
			'}';
	}
}
