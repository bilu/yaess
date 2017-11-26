package pl.biltec.yaess.clp.ports.account;

import pl.biltec.yaess.clp.domain.event.CustomerDeletedEvent;
import pl.biltec.yaess.clp.ports.account.command.DeactivateAccountCommand;


/**
 * <pre>
 * <b>Purpose:</b>
 * 	Receives events from other contexts and invokes *CommandService in order to perform operation
 * <b>Question:</b>
 * 	Where it should be located? Is it port or is it adapter?
 * </pre>
 *
 */
public class AccountEventHandler {

	private AccountCommandService accountCommandService;

	public AccountEventHandler(AccountCommandService accountCommandService) {

		this.accountCommandService = accountCommandService;
	}

	public void handle(CustomerDeletedEvent event) {

		accountCommandService.handle(new DeactivateAccountCommand(event.rootAggregateId().toString(), event.originator()));
	}
}
