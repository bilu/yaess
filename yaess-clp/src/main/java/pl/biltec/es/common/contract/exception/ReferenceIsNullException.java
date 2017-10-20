package pl.biltec.es.common.contract.exception;


public class ReferenceIsNullException extends ContractBrokenException {

	private static final long serialVersionUID = 1L;

	public ReferenceIsNullException(String objectName) {
		super("'" + objectName + "' can't be null");
	}
}