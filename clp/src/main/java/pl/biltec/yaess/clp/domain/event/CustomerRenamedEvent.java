package pl.biltec.yaess.clp.domain.event;

import java.time.LocalDateTime;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerRenamedEvent extends Event {

	private String newName;

	public CustomerRenamedEvent(RootAggregateId id, String newName, LocalDateTime timestamp) {

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
			", rootAggregateId=" + rootAggregateId +
			", version=" + version +
			", created=" + created +
			'}';
	}
}
