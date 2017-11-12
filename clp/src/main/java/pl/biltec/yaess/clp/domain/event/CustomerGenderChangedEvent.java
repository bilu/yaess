package pl.biltec.yaess.clp.domain.event;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


/** Kept only for backward compatibility */
@Deprecated
public class CustomerGenderChangedEvent extends Event {

	private String gender;

	public CustomerGenderChangedEvent(RootAggregateId id, String gender, LocalDateTime now, String originator) {

		super(id, now, originator);
		this.gender = gender;
	}

	public String getGender() {

		return gender;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof CustomerGenderChangedEvent))
			return false;

		CustomerGenderChangedEvent that = (CustomerGenderChangedEvent) o;

		return new EqualsBuilder()
			.appendSuper(super.equals(o))
			.append(gender, that.gender)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.appendSuper(super.hashCode())
			.append(gender)
			.toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("gender", gender)
			.append("customerId", rootAggregateId)
			.append("eventId", eventId)
			.append("created", created)
			.toString();
	}
}
