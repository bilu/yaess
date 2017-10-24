package pl.biltec.yaess.clp.domain.event;

import java.time.LocalDateTime;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerCreatedEvent extends Event {

	private String name;

	public CustomerCreatedEvent(RootAggregateId id, String name, LocalDateTime created) {

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
			", rootAggregateId=" + rootAggregateId +
			", version=" + version +
			", created=" + created +
			'}';
	}
}
