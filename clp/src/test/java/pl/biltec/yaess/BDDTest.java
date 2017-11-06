package pl.biltec.yaess;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.apache.commons.lang3.reflect.MethodUtils;

import pl.biltec.yaess.clp.ports.customer.Command;


/**
 * <pre>
 * COMMAND_SERVICE have to have handle(x extends Command) methods
 * </pre>
 * @param <SELF_TYPE>
 * @param <COMMAND_SERVICE>
 */
public abstract class BDDTest<SELF_TYPE extends BDDTest<SELF_TYPE, COMMAND_SERVICE>, COMMAND_SERVICE> {

	private COMMAND_SERVICE commandService;
	private Optional<Exception> exceptionThrownOptional = Optional.empty();

	protected SELF_TYPE self() {
		return (SELF_TYPE) this;
	}

	public BDDTest(COMMAND_SERVICE commandService) {

		this.commandService = commandService;
	}


	public SELF_TYPE given(Command... commands) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if(commands != null) {
			for (int i = 0; i < commands.length; i++) {
				MethodUtils.invokeExactMethod(commandService, "handle", commands[i]);
			}
		}
		return self();
	}

	public SELF_TYPE given(Command command) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		MethodUtils.invokeExactMethod(commandService, "handle", command);
		return self();
	}

	public SELF_TYPE and(Command command) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		MethodUtils.invokeExactMethod(commandService, "handle", command);
		return self();
	}

	public void when(Command... commands) {

		try {

		}
		catch (Exception e) {
			//only last exception
			if (!exceptionThrownOptional.isPresent()) {
				exceptionThrownOptional = Optional.of(e);
			}
		}

	}


}
