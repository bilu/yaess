package pl.biltec.yaess.core.common.exception;


public class TextIsNullOrEmptyException extends ContractBrokenException {

	private static final long serialVersionUID = 1L;

	public TextIsNullOrEmptyException(String objectName) {
		super("'" + objectName + "' can't be null or empty");
	}
}