package pl.biltec.yaess.clp.domain.customer.exception;

import pl.biltec.yaess.clp.domain.customer.CustomerId;


public class CustomerNotExistsException extends DomainOperationException {

	public CustomerNotExistsException(CustomerId customerId) {

		super(customerId);
	}
}
