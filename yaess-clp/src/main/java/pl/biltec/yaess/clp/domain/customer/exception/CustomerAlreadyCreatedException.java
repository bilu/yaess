package pl.biltec.yaess.clp.domain.customer.exception;

import pl.biltec.yaess.clp.domain.customer.CustomerId;


public class CustomerAlreadyCreatedException extends DomainOperationException {

	public CustomerAlreadyCreatedException(CustomerId customerId) {

		super(customerId);
	}
}
