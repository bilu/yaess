package pl.biltec.yaess.clp.ports.customer.command;

import pl.biltec.yaess.clp.domain.customer.CustomerId;


public class DeleteCustomerCommand extends CustomerCommand {

	private CustomerId id;

	public DeleteCustomerCommand(CustomerId id) {

		this.id = id;
	}

	@Override
	public CustomerId getId() {

		return id;
	}

	@Override
	public String toString() {

		return "DeleteCustomerCommand{" +
			"id=" + id +
			'}';
	}
}
