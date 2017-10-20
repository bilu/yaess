package pl.biltec.yaess.yaess.core.common.contract.exception;


public class ConditionNotMetException extends ContractBrokenException {

	private static final long serialVersionUID = 1L;

	public ConditionNotMetException(String message) {
		super(message);
	}
}