package pl.biltec.yaess.yaess.core.common.contract.exception;


import java.util.Arrays;

public class IncorrectValueException extends ContractBrokenException {

	private static final long serialVersionUID = 1L;

	public IncorrectValueException(String objectName, Object value) {
		super(String.format("'%s' has incorrect value '%s'", objectName, value));
	}

	public IncorrectValueException(String objectName, Object value, Object... expected) {
		super(String.format("'%s' has incorrect value '%s', expected '%s'", objectName, value, Arrays.asList(expected)));
	}

}