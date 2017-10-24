package pl.biltec.yaess.clp.ports.customer.command;

import pl.biltec.yaess.core.domain.RootAggregateId;


public abstract class CustomerCommand {

	public abstract RootAggregateId getId();

	@Override
	public abstract String toString();
}
