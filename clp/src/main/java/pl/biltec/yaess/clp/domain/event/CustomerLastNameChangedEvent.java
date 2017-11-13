package pl.biltec.yaess.clp.domain.event;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerLastNameChangedEvent extends Event {

	private String lastName;

	public CustomerLastNameChangedEvent(RootAggregateId id, String lastName, LocalDateTime timestamp, String originator) {

		super(id, timestamp, originator);
		this.lastName = lastName;
	}

	public String getLastName() {

		return lastName;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof CustomerLastNameChangedEvent))
			return false;

		CustomerLastNameChangedEvent that = (CustomerLastNameChangedEvent) o;

		return new EqualsBuilder()
			.appendSuper(super.equals(that))
			.append(lastName, that.lastName)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.appendSuper(super.hashCode())
			.append(lastName)
			.toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("lastName", lastName)
			.append("customerId", rootAggregateId)
			.append("eventId", eventId)
			.append("created", created)
			.toString();
	}
}
