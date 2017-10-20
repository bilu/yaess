
package pl.biltec.loyalyty.application.customer.command.exception;

import pl.biltec.loyalyty.application.customer.command.CustomerCommand;


public class UnsupportedCommandException extends RuntimeException {

	public UnsupportedCommandException(CustomerCommand command) {

		super(String.format("No support for command %s defined", command.getClass()));

	}
}
