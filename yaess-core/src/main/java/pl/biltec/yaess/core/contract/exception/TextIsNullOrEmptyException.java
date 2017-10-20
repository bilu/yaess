package pl.biltec.yaess.yaess.core.common.contract.exception;


public class TextIsNullOrEmptyException extends ContractBrokenException {

	private static final long serialVersionUID = 1L;

	public TextIsNullOrEmptyException(String objectName) {
		super("'" + objectName + "' can't be null or empty");
	}
}