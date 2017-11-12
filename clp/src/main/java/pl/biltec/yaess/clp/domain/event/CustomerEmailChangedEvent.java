package pl.biltec.yaess.clp.domain.event;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


/** Kept only for backward compatibility */
@Deprecated
public class CustomerEmailChangedEvent extends Event {

	private String email;

	public CustomerEmailChangedEvent(RootAggregateId id, String email, LocalDateTime now, String originator) {

		super(id, now, originator);
		this.email = email;
	}

	public String getEmail() {

		return email;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof CustomerEmailChangedEvent))
			return false;

		CustomerEmailChangedEvent that = (CustomerEmailChangedEvent) o;

		return new EqualsBuilder()
			.appendSuper(super.equals(o))
			.append(email, that.email)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.appendSuper(super.hashCode())
			.append(email)
			.toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("email", email)
			.append("customerId", rootAggregateId)
			.append("eventId", eventId)
			.append("created", created)
			.toString();
	}
}
