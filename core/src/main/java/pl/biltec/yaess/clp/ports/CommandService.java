package pl.biltec.yaess.clp.ports;

import static pl.biltec.yaess.core.common.Contract.notNull;

import java.util.function.Consumer;

import pl.biltec.yaess.core.common.Contract;
import pl.biltec.yaess.core.domain.Repository;
import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


public abstract class CommandService<ROOT_AGGREGATE_REPOSITORY extends Repository<ROOT_AGGREGATE>, ROOT_AGGREGATE extends RootAggregate> {

	protected ROOT_AGGREGATE_REPOSITORY repository;
	protected AuthorizationService authorizationService;

	public CommandService(ROOT_AGGREGATE_REPOSITORY repository, AuthorizationService authorizationService) {

		this.repository = Contract.notNull(repository, "repository");
		this.authorizationService = Contract.notNull(authorizationService, "authorizationService");
	}

	protected void action(Command command, Consumer<ROOT_AGGREGATE> action) {

		notNull(command, "command");
		authorizationService.checkAuthorization(command);
		ROOT_AGGREGATE account = repository.get(new RootAggregateId(command.getRootAggregateId()));
		action.accept(account);
		repository.save(account);
	}

}
