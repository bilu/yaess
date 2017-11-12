package pl.biltec.yaess.clp.ports.customer.command;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.clp.ports.customer.Command;


public class CreateCustomerCommand extends Command {

	private String firstName;
	private String surname;
	private String email;
	private String personalIdNumber;

	public CreateCustomerCommand(String customerId, String originator, String firstName, String surname, String email, String personalIdNumber) {

		super(originator, customerId);
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

		if (!(o instanceof CreateCustomerCommand))
			return false;

		CreateCustomerCommand that = (CreateCustomerCommand) o;

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
			.append("originator", originator)
			.append("rootAggregateId", rootAggregateId)
			.toString();
	}
}
