package pl.biltec.yaess.clp.ports.customer.command;

import pl.biltec.yaess.core.domain.RootAggregateId;


// TODO [bilu] 23.10.17 remove inheritance
public class CreateCustomerCommand extends CustomerCommand {

	private String name;
	// TODO [bilu] 23.10.17 remove me 
	private RootAggregateId id;

	public CreateCustomerCommand(String name, RootAggregateId id) {

		this.name = name;
		this.id = id;
	}

	@Override
	public RootAggregateId getId() {

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
