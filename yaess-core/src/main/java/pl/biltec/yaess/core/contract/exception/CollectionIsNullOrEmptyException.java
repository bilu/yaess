package pl.biltec.yaess.yaess.core.common.contract.exception;


public class CollectionIsNullOrEmptyException extends ContractBrokenException {

	private static final long serialVersionUID = 1L;

	public CollectionIsNullOrEmptyException(String objectName) {
		super("'" + objectName + "' can't be null or empty");
	}

}
