package pl.biltec.yaess.clp.ports.customer.command;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.clp.ports.Command;


public class ChangeCustomerNameCommand extends Command {

	private String firstName;
	private String lastName;

	public ChangeCustomerNameCommand(String customerId, String originator, String firstName, String lastName) {

		super(originator, customerId);
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {

		return firstName;
	}

	public String getLastName() {

		return lastName;
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
			.append(lastName, that.lastName)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.appendSuper(super.hashCode())
			.append(firstName)
			.append(lastName)
			.toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("firstName", firstName)
			.append("lastName", lastName)
			.append("originator", originator)
			.append("customerId", rootAggregateId)
			.toString();
	}
}
