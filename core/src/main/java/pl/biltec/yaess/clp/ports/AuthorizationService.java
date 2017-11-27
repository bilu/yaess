package pl.biltec.yaess.clp.ports;

import pl.biltec.yaess.core.common.Contract;
import pl.biltec.yaess.core.common.exception.ConditionNotMetException;



public interface AuthorizationService {

	boolean isAllowedToInvokeCommand(Command command);

	default void checkAuthorization(Command command) {
		Contract.isTrue(
			isAllowedToInvokeCommand(command),
			() -> new ConditionNotMetException("User " + command.originator + " is not allowed to perform " + command));
	}
}
