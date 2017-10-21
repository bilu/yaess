package pl.biltec.yaess.core.common.exception;


public class CollectionContainsNullValueException extends ContractBrokenException {
	private static final long serialVersionUID = 1L;
	
	public CollectionContainsNullValueException(String objectName) {
		super("'" + objectName + "' can't contain null values");
	}
}
