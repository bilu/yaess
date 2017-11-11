package pl.biltec.yaess;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import org.junit.Before;

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

	@Before
	public void setUp() throws Exception {

		commandService = prepare();
		exceptionThrownOptional = Optional.empty();
	}

	public abstract COMMAND_SERVICE prepare();

	public SELF_TYPE given(Command... commands) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		invokeMultipleCommands(commands);
		return self();
	}

	private void invokeMultipleCommands(Command[] commands) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		if (commands != null) {
			for (int i = 0; i < commands.length; i++) {
				invokeSingleCommand(commands[i]);
			}
		}
	}

	private void invokeSingleCommand(Command command) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		MethodUtils.invokeExactMethod(commandService, "handle", command);
	}

	public SELF_TYPE and(Command... commands) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		invokeMultipleCommands(commands);
		return self();
	}

	public SELF_TYPE when(Command... commands) {

		try {
			invokeMultipleCommands(commands);
		}
		catch (Exception e) {
			//only last exception
			if (!exceptionThrownOptional.isPresent()) {
				exceptionThrownOptional = Optional.of(e);
			}
		}
		return self();
	}

	public SELF_TYPE thenThrowNoException() {

		if (exceptionThrownOptional.isPresent()) {
			Exception e = exceptionThrownOptional.get();
			// TODO [bilu] 06.11.17 more detailed stack
			Fail.fail("Expected no exception, found exception of class " + e.getClass() + " with message " + e.getMessage() + ". Caused by " + e.getCause());
		}
		return self();
	}

	public SELF_TYPE thenThrow(Class<? extends Exception> exceptionClass, String hasMessageContaining) {

		Throwable throwable = exceptionThrownOptional
			.map(Throwable::getCause)
			.orElseThrow(() -> new AssertionError("Expected exception of class " + exceptionClass + " with message containing " + hasMessageContaining));
		Assertions.assertThat(throwable).isInstanceOf(exceptionClass).hasMessageContaining(hasMessageContaining);

		return self();
	}

	public SELF_TYPE thenThrow(Class<? extends Exception> exceptionClass) {

		Throwable throwable = exceptionThrownOptional
			.map(Throwable::getCause)
			.orElseThrow(() -> new AssertionError("Expected exception of class " + exceptionClass));
		Assertions.assertThat(throwable).isInstanceOf(exceptionClass);
		return self();
	}

	public String givenRandomId() {

		return UUID.randomUUID().toString();
	}

	public SELF_TYPE sleep(int millis) {

		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		return self();
	}
}
