package pl.biltec.yaess.clp.domain.customer.exception;

import pl.biltec.yaess.clp.domain.customer.CustomerId;


public class ConcurrentModificationException extends DomainOperationException {

	public ConcurrentModificationException(CustomerId customerId, long expectedConcurrencyVersion, long actualConcurrencyVersion) {

		super(customerId, String.format("Expected Concurrency Version: %s, found %s", expectedConcurrencyVersion, actualConcurrencyVersion));
	}
}
