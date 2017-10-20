package pl.biltec.yaess.clp.ports.customer.command;

import pl.biltec.yaess.clp.domain.customer.CustomerId;


public abstract class CustomerCommand {

	public abstract CustomerId getId();

	@Override
	public abstract String toString();
}
