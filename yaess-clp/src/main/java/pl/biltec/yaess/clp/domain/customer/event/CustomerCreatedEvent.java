package pl.biltec.yaess.clp.domain.customer.event;

import java.time.LocalDateTime;

import pl.biltec.yaess.clp.domain.customer.CustomerId;


public class CustomerCreatedEvent extends CustomerEvent {

	private String name;

	public CustomerCreatedEvent(CustomerId id, String name, LocalDateTime created) {

		super(id, created);
		this.name = name;
	}

	public String getName() {

		return name;
	}

	@Override
	public String toString() {

		return "CustomerCreatedEvent{" +
			"name='" + name + '\'' +
			", id=" + id +
			", version=" + version +
			", created=" + created +
			'}';
	}
}
