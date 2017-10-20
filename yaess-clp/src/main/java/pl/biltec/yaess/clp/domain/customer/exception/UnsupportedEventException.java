package pl.biltec.yaess.clp.domain.customer.exception;

import pl.biltec.yaess.clp.domain.customer.event.CustomerEvent;


public class UnsupportedEventException extends RuntimeException {

	public UnsupportedEventException(CustomerEvent event) {

		super(String.format("No support for event %s defined", event.getClass()));
	}
}
