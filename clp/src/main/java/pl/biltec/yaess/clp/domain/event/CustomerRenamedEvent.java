package pl.biltec.yaess.clp.domain.event;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof CustomerRenamedEvent))
			return false;

		CustomerRenamedEvent that = (CustomerRenamedEvent) o;

		return new EqualsBuilder()
			.appendSuper(super.equals(that))
			.append(newName, that.newName)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.appendSuper(super.hashCode())
			.append(newName)
			.toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("newName", newName)
			.append("rootAggregateId", rootAggregateId)
			.append("eventID", eventID)
			.append("version", version)
			.append("created", created)
			.toString();
	}
}
