package pl.biltec.yaess.clp.domain.event;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerDeletedEvent extends Event {


	public CustomerDeletedEvent(RootAggregateId id, LocalDateTime timestamp) {

		super(id, timestamp);
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("rootAggregateId", rootAggregateId)
			.append("eventID", eventID)
			.append("version", version)
			.append("created", created)
			.toString();
	}
}
