package pl.biltec.loyalyty.domain.customer.exception;

import pl.biltec.loyalyty.domain.customer.CustomerId;


public class ConcurrentModificationException extends DomainOperationException {

	public ConcurrentModificationException(CustomerId customerId, int expectedConcurrencyVersion, Integer actualConcurrencyVersion) {

		super(customerId, String.format("Expected Concurrency Version: %s, found %s", expectedConcurrencyVersion, actualConcurrencyVersion));
	}
}
