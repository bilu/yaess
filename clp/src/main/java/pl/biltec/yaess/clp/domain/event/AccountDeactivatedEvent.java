package pl.biltec.yaess.clp.domain.event;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class AccountDeactivatedEvent extends Event {

	public AccountDeactivatedEvent(RootAggregateId id, LocalDateTime created, String originator) {

		super(id, created, originator);
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("rootAggregateId", rootAggregateId)
			.append("eventId", eventId)
			.append("created", created)
			.append("originator", originator)
			.toString();
	}
}
