package pl.biltec.yaess.clp.ports.customer.command;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.clp.ports.Command;


public class DeleteCustomerCommand extends Command {


	public DeleteCustomerCommand(String customerId, String originator) {

		super(originator, customerId);
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof DeleteCustomerCommand))
			return false;

		DeleteCustomerCommand that = (DeleteCustomerCommand) o;

		return new EqualsBuilder()
			.appendSuper(super.equals(o))
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.appendSuper(super.hashCode())
			.toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("originator", originator)
			.append("customerId", rootAggregateId)
			.toString();
	}
}
