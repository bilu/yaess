package pl.biltec.yaess.core.common.exception;

public class ConcurrentModificationException extends DomainOperationException {

	public ConcurrentModificationException(String id, long expectedConcurrencyVersion, long actualConcurrencyVersion) {

		super(id, String.format("Expected Concurrency Version: %s, found %s", expectedConcurrencyVersion, actualConcurrencyVersion));
	}
}
