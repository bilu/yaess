package pl.biltec.yaess.clp.domain.customer.exception;

import pl.biltec.yaess.clp.domain.customer.CustomerId;


public abstract class DomainOperationException extends RuntimeException {

	// TODO: [pbilewic] 08.10.17 może dodać klasę
	public DomainOperationException(CustomerId customerId) {

		super("[ID=" + customerId + "]");
	}

	public DomainOperationException(CustomerId customerId, String message) {

		super("[ID=" + customerId + "]: " + message);
	}
}
