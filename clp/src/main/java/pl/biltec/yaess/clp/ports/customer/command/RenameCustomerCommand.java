package pl.biltec.yaess.clp.ports.customer.command;

import pl.biltec.yaess.core.domain.RootAggregateId;


public class RenameCustomerCommand extends CustomerCommand {

	private String newName;
	private RootAggregateId id;

	public RenameCustomerCommand(String newName, RootAggregateId id) {

		this.newName = newName;
		this.id = id;
	}

	@Override
	public RootAggregateId getId() {

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
