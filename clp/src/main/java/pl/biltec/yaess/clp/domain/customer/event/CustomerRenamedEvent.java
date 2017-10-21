package pl.biltec.yaess.clp.domain.customer.event;

import java.time.LocalDateTime;

import pl.biltec.yaess.clp.domain.customer.CustomerId;


public class CustomerRenamedEvent extends CustomerEvent {

	private String newName;

	public CustomerRenamedEvent(CustomerId id, String newName, LocalDateTime timestamp) {

		super(id, timestamp);
		this.newName = newName;
	}

	public String getNewName() {

		return newName;
	}

	@Override
	public String toString() {

		return "CustomerRenamedEvent{" +
			"newName='" + newName + '\'' +
			", id=" + id +
			", version=" + version +
			", created=" + created +
			'}';
	}
}
