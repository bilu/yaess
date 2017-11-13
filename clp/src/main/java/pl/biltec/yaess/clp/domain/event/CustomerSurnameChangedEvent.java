package pl.biltec.yaess.clp.domain.event;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;

/** Kept only for backward compatibility */
@Deprecated
public class CustomerSurnameChangedEvent extends Event {

	private String surname;

	public CustomerSurnameChangedEvent(RootAggregateId id, String surname, LocalDateTime timestamp, String originator) {

		super(id, timestamp, originator);
		this.surname = surname;
	}

	public String getSurname() {

		return surname;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof CustomerSurnameChangedEvent))
			return false;

		CustomerSurnameChangedEvent that = (CustomerSurnameChangedEvent) o;

		return new EqualsBuilder()
			.appendSuper(super.equals(that))
			.append(surname, that.surname)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.appendSuper(super.hashCode())
			.append(surname)
			.toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("surname", surname)
			.append("customerId", rootAggregateId)
			.append("eventId", eventId)
			.append("created", created)
			.toString();
	}
}
