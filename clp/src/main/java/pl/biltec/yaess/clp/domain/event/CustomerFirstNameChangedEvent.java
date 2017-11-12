package pl.biltec.yaess.clp.domain.event;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerFirstNameChangedEvent extends Event {

	private String firstName;

	public CustomerFirstNameChangedEvent(RootAggregateId id, String firstName, LocalDateTime timestamp, String originator) {

		super(id, timestamp, originator);
		this.firstName = firstName;
	}

	public String getFirstName() {

		return firstName;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof CustomerFirstNameChangedEvent))
			return false;

		CustomerFirstNameChangedEvent that = (CustomerFirstNameChangedEvent) o;

		return new EqualsBuilder()
			.appendSuper(super.equals(that))
			.append(firstName, that.firstName)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.appendSuper(super.hashCode())
			.append(firstName)
			.toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("firstName", firstName)
			.append("customerId", rootAggregateId)
			.append("eventId", eventId)
			.append("created", created)
			.toString();
	}
}
