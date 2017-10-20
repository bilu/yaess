package pl.biltec.loyalyty.domain.customer.event;

import java.time.LocalDateTime;

import pl.biltec.loyalyty.domain.customer.CustomerId;


public class CustomerRenamedEvent extends CustomerEvent {

	private String newName;

	public CustomerRenamedEvent(CustomerId customerId, String newName, LocalDateTime timestamp) {

		super(customerId, timestamp);
		this.newName = newName;
	}

	public String getNewName() {

		return newName;
	}

	@Override
	public String toString() {

		return "CustomerRenamedEvent{" +
			"newName='" + newName + '\'' +
			", customerId=" + customerId +
			", created=" + created +
			'}';
	}
}
