package pl.biltec.yaess.clp.domain.event;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerDeletedEvent extends Event {


	public CustomerDeletedEvent(RootAggregateId id, LocalDateTime timestamp, String originator) {

		super(id, timestamp, originator);
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("customerId", rootAggregateId)
			.append("eventId", eventId)
			.append("created", created)
			.toString();
	}
}
