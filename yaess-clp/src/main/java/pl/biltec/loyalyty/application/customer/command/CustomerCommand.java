package pl.biltec.loyalyty.application.customer.command;

import pl.biltec.loyalyty.domain.customer.CustomerId;


public abstract class CustomerCommand {

	public abstract CustomerId getId();

	@Override
	public abstract String toString();
}
