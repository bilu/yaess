package pl.biltec.yaess.clp.ports.customer.command;

import pl.biltec.yaess.core.domain.RootAggregateId;


public class DeleteCustomerCommand extends CustomerCommand {

	private RootAggregateId id;

	public DeleteCustomerCommand(RootAggregateId id) {

		this.id = id;
	}

	@Override
	public RootAggregateId getId() {

		return id;
	}

	@Override
	public String toString() {

		return "DeleteCustomerCommand{" +
			"rootAggregateId=" + id +
			'}';
	}
}
