package pl.biltec.yaess.clp.domain.event;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerCreatedEvent extends Event {

	private String name;
	private String email;
	private String personalIdNumber;

	public CustomerCreatedEvent(RootAggregateId id, String name, String email, String personalIdNumber, LocalDateTime created, String originator) {

		super(id, created, originator);
		this.name = name;
		this.email = email;
		this.personalIdNumber = personalIdNumber;
	}

	public String getName() {

		return name;
	}

	public String getEmail() {

		return email;
	}

	public String getPersonalIdNumber() {

		return personalIdNumber;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof CustomerCreatedEvent))
			return false;

		CustomerCreatedEvent that = (CustomerCreatedEvent) o;

		return new EqualsBuilder()
			.appendSuper(super.equals(o))
			.append(name, that.name)
			.append(email, that.email)
			.append(personalIdNumber, that.personalIdNumber)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.appendSuper(super.hashCode())
			.append(name)
			.append(email)
			.append(personalIdNumber)
			.toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("name", name)
			.append("email", email)
			.append("personalIdNumber", personalIdNumber)
			.append("rootAggregateId", rootAggregateId)
			.append("eventID", eventID)
			.append("version", version)
			.append("created", created)
			.append("originator", originator)
			.toString();
	}
}
