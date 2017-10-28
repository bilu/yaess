package pl.biltec.yaess.clp.ports.customer.command;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.clp.ports.customer.Command;


public class ChangeCustomerNameCommand extends Command {

	private String name;

	public ChangeCustomerNameCommand(String customerId, String originator, String name) {

		super(originator, customerId);
		this.name = name;
	}

	public String getName() {

		return name;
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
			.append(name, that.name)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.appendSuper(super.hashCode())
			.append(name)
			.toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("name", name)
			.append("originator", originator)
			.append("customerId", rootAggregateId)
			.toString();
	}
}
