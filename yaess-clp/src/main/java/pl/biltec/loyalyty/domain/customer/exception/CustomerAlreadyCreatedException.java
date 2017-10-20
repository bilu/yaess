package pl.biltec.loyalyty.domain.customer.exception;

import pl.biltec.loyalyty.domain.customer.CustomerId;


public class CustomerAlreadyCreatedException extends DomainOperationException {

	public CustomerAlreadyCreatedException(CustomerId customerId) {

		super(customerId);
	}
}
