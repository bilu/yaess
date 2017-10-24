package pl.biltec.yaess.core.common.exception;

public class UnsupportedEventException extends RuntimeException {

	public UnsupportedEventException(Object event) {

		super(String.format("No support for event %s defined", event.getClass()));
	}
}
