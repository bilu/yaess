package pl.biltec.loyalyty.application.customer.command;

import pl.biltec.loyalyty.domain.customer.CustomerId;


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
			", id=" + id +
			'}';
	}
}
