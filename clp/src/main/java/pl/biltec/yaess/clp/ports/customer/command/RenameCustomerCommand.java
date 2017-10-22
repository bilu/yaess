package pl.biltec.yaess.clp.ports.customer.command;

import pl.biltec.yaess.clp.domain.customer.CustomerId;


public class RenameCustomerCommand extends CustomerCommand {

	private String newName;
	private CustomerId id;

	public RenameCustomerCommand(String newName, CustomerId id) {

		this.newName = newName;
		this.id = id;
	}

	@Override
	public CustomerId getId() {

		return id;
	}

	public String getNewName() {

		return newName;
	}

	@Override
	public String toString() {

		return "CreateCustomerCommand{" +
			"newName='" + newName + '\'' +
			", rootAggregateId=" + id +
			'}';
	}
}
