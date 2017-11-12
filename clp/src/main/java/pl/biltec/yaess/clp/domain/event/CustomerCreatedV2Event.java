package pl.biltec.yaess.clp.domain.event;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerCreatedV2Event extends Event {

	private String firstName;
	private String surname;
	private String email;
	private String personalIdNumber;

	public CustomerCreatedV2Event(RootAggregateId id, String firstName, String surname, String email, String personalIdNumber, LocalDateTime created, String originator) {

		super(id, created, originator);
		this.firstName = firstName;
		this.surname = surname;
		this.email = email;
		this.personalIdNumber = personalIdNumber;
	}

	public String getFirstName() {

		return firstName;
	}

	public String getSurname() {

		return surname;
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

		if (!(o instanceof CustomerCreatedV2Event))
			return false;

		CustomerCreatedV2Event that = (CustomerCreatedV2Event) o;

		return new EqualsBuilder()
			.appendSuper(super.equals(o))
			.append(firstName, that.firstName)
			.append(surname, that.surname)
			.append(email, that.email)
			.append(personalIdNumber, that.personalIdNumber)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.appendSuper(super.hashCode())
			.append(firstName)
			.append(surname)
			.append(email)
			.append(personalIdNumber)
			.toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("firstName", firstName)
			.append("surname", surname)
			.append("email", email)
			.append("personalIdNumber", personalIdNumber)
			.append("rootAggregateId", rootAggregateId)
			.append("eventId", eventId)
			.append("created", created)
			.append("originator", originator)
			.toString();
	}
}
