package pl.biltec.yaess.core.common.exception;


public class ConditionNotMetException extends ContractBrokenException {

	private static final long serialVersionUID = 1L;

	public ConditionNotMetException(String message) {
		super(message);
	}
}