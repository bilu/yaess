package pl.biltec.yaess.core.common.exception;

public abstract class DomainOperationException extends RuntimeException {

	public DomainOperationException(String id) {

		super("[ID=" + id + "]");
	}

	public DomainOperationException(String id, String message) {

		super("[ID=" + id + "]: " + message);
	}
}
