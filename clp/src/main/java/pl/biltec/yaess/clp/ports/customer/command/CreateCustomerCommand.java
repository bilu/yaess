package pl.biltec.yaess.clp.ports.customer.command;

import pl.biltec.yaess.clp.domain.customer.CustomerId;


public class CreateCustomerCommand extends CustomerCommand {

	private String name;
	private CustomerId id;

	public CreateCustomerCommand(String name, CustomerId id) {

		this.name = name;
		this.id = id;
	}

	@Override
	public CustomerId getId() {

		return id;
	}

	public String getName() {

		return name;
	}

	@Override
	public String toString() {

		return "CreateCustomerCommand{" +
			"newName='" + name + '\'' +
			", rootAggregateId=" + id +
			'}';
	}
}
