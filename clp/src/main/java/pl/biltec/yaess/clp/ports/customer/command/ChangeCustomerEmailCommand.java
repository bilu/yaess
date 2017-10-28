package pl.biltec.yaess.clp.ports.customer.command;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.clp.ports.customer.Command;


public class ChangeCustomerEmailCommand extends Command {

	private String email;

	public ChangeCustomerEmailCommand(String customerId, String originator, String email) {

		super(originator, customerId);
		this.email = email;
	}

	public String getEmail() {

		return email;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof ChangeCustomerEmailCommand))
			return false;

		ChangeCustomerEmailCommand that = (ChangeCustomerEmailCommand) o;

		return new EqualsBuilder()
			.appendSuper(super.equals(o))
			.append(email, that.email)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.appendSuper(super.hashCode())
			.append(email)
			.toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("email", email)
			.append("originator", originator)
			.append("customerId", rootAggregateId)
			.toString();
	}
}
