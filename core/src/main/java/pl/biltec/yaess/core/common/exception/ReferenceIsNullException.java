package pl.biltec.yaess.core.common.exception;


public class ReferenceIsNullException extends ContractBrokenException {

	private static final long serialVersionUID = 1L;

	public ReferenceIsNullException(String objectName) {
		super("'" + objectName + "' can't be null");
	}
}