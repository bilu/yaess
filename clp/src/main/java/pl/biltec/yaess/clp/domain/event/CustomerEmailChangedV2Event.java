package pl.biltec.yaess.clp.domain.event;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerEmailChangedV2Event extends Event{

	private String newEmail;
	private String oldEmail;

	public CustomerEmailChangedV2Event(RootAggregateId id, String oldEmail, String newEmail, LocalDateTime now, String originator) {
		super(id, now, originator);
		this.newEmail = newEmail;
		this.oldEmail = oldEmail;
	}

	public String getNewEmail() {

		return newEmail;
	}

	public String getOldEmail() {

		return oldEmail;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof CustomerEmailChangedV2Event))
			return false;

		CustomerEmailChangedV2Event that = (CustomerEmailChangedV2Event) o;

		return new EqualsBuilder()
			.appendSuper(super.equals(o))
			.append(newEmail, that.newEmail)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.appendSuper(super.hashCode())
			.append(newEmail)
			.toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("newEmail", newEmail)
			.append("oldEmail", oldEmail)
			.append("customerId", rootAggregateId)
			.append("eventId", eventId)
			.append("created", created)
			.toString();
	}
}
