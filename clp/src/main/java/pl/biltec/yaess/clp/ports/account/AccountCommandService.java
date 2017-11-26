package pl.biltec.yaess.clp.ports.account;

import pl.biltec.yaess.clp.domain.account.Account;
import pl.biltec.yaess.clp.domain.account.AccountRepository;
import pl.biltec.yaess.clp.domain.account.Points;
import pl.biltec.yaess.clp.ports.CommandService;
import pl.biltec.yaess.clp.ports.AuthorizationService;
import pl.biltec.yaess.clp.ports.account.command.AddPointsToAccountCommand;
import pl.biltec.yaess.clp.ports.account.command.DeactivateAccountCommand;
import pl.biltec.yaess.clp.ports.account.command.SubtractPointsFromAccountCommand;


public class AccountCommandService extends CommandService<AccountRepository, Account> {

	public AccountCommandService(AccountRepository accountRepository, AuthorizationService authorizationService) {
		super(accountRepository, authorizationService);
	}

	public void handle(DeactivateAccountCommand command) {
		action(command, account -> account.deactivate(command.getOriginator()));
	}

	public void handle(AddPointsToAccountCommand command) {
		action(command, account -> account.addPoints(new Points(command.getPoints()), command.getOriginator()));
	}

	public void handle(SubtractPointsFromAccountCommand command) {
		action(command, account -> account.subtractPoints(new Points(command.getPoints()), command.getOriginator()));
	}

}
