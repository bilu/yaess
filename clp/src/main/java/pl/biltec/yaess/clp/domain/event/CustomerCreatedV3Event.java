package pl.biltec.yaess.clp.domain.event;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerCreatedV3Event extends Event {

	private String firstName;
	private String lastName;
	private String email;
	private String personalIdNumber;

	public CustomerCreatedV3Event(RootAggregateId id, String firstName, String lastName, String email, String personalIdNumber, LocalDateTime created, String originator) {

		super(id, created, originator);
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.personalIdNumber = personalIdNumber;
	}

	public String getFirstName() {

		return firstName;
	}

	public String getLastName() {

		return lastName;
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

		if (!(o instanceof CustomerCreatedV3Event))
			return false;

		CustomerCreatedV3Event that = (CustomerCreatedV3Event) o;

		return new EqualsBuilder()
			.appendSuper(super.equals(o))
			.append(firstName, that.firstName)
			.append(lastName, that.lastName)
			.append(email, that.email)
			.append(personalIdNumber, that.personalIdNumber)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.appendSuper(super.hashCode())
			.append(firstName)
			.append(lastName)
			.append(email)
			.append(personalIdNumber)
			.toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("firstName", firstName)
			.append("lastName", lastName)
			.append("email", email)
			.append("personalIdNumber", personalIdNumber)
			.append("rootAggregateId", rootAggregateId)
			.append("eventId", eventId)
			.append("created", created)
			.append("originator", originator)
			.toString();
	}
}
