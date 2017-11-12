package pl.biltec.yaess.clp.ports.customer.command;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.clp.ports.customer.Command;


public class ChangeCustomerNameCommand extends Command {

	private String firstName;
	private String surname;

	public ChangeCustomerNameCommand(String customerId, String originator, String firstName, String surname) {

		super(originator, customerId);
		this.firstName = firstName;
		this.surname = surname;
	}

	public String getFirstName() {

		return firstName;
	}

	public String getSurname() {

		return surname;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof ChangeCustomerNameCommand))
			return false;

		ChangeCustomerNameCommand that = (ChangeCustomerNameCommand) o;

		return new EqualsBuilder()
			.appendSuper(super.equals(o))
			.append(firstName, that.firstName)
			.append(surname, that.surname)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.appendSuper(super.hashCode())
			.append(firstName)
			.append(surname)
			.toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("firstName", firstName)
			.append("surname", surname)
			.append("originator", originator)
			.append("customerId", rootAggregateId)
			.toString();
	}
}
