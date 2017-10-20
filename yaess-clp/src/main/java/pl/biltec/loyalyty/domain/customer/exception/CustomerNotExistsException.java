package pl.biltec.loyalyty.domain.customer.exception;

import pl.biltec.loyalyty.domain.customer.CustomerId;


public class CustomerNotExistsException extends DomainOperationException {

	public CustomerNotExistsException(CustomerId customerId) {

		super(customerId);
	}
}
