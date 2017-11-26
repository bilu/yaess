package pl.biltec.yaess.clp.ports.customer;

import static pl.biltec.yaess.core.common.Contract.isTrue;
import static pl.biltec.yaess.core.common.Contract.notNull;

import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.customer.CustomerRepository;
import pl.biltec.yaess.clp.domain.customer.exception.CustomerAlreadyCreatedException;
import pl.biltec.yaess.clp.ports.AuthorizationService;
import pl.biltec.yaess.clp.ports.CommandService;
import pl.biltec.yaess.clp.ports.customer.command.ChangeCustomerEmailCommand;
import pl.biltec.yaess.clp.ports.customer.command.ChangeCustomerNameCommand;
import pl.biltec.yaess.clp.ports.customer.command.CreateCustomerCommand;
import pl.biltec.yaess.clp.ports.customer.command.DeleteCustomerCommand;
import pl.biltec.yaess.core.common.Contract;


/**
 * <pre>
 * <b>Purpose:</b>
 * 	Public API / Entry point to interact with Customer domain
 * <b>Question:</b>
 * 	Do i really need Commands, why can't i use "normal, descriptive methods"?
 * </pre>
 *
 */
public class CustomerCommandService extends CommandService<CustomerRepository, Customer> {

	public CustomerCommandService(CustomerRepository repository, AuthorizationService authorizationService) {
		super(repository, authorizationService);
	}

	public void handle(CreateCustomerCommand command) {

		notNull(command, "command");
		authorizationService.checkAuthorization(command);
		isTrue(repository.isEmailUnique(command.getEmail()), "Email " + command.getEmail() + " already occupied");
		Customer customer = new Customer(command.getRootAggregateId(), command.getFirstName(), command.getLastName(), command.getEmail(), command.getPersonalIdNumber(), command.getOriginator());
		// TODO [bilu] 28.10.17  unify type of exception ContractBroken vs DomainOperation
		if (repository.exists(customer.id())) {
			throw new CustomerAlreadyCreatedException(customer.id());
		}
		repository.save(customer);
	}

	public void handle(ChangeCustomerNameCommand command) {

		action(command, customer -> {
			customer.changeFirstName(command.getFirstName(), command.getOriginator());
			customer.changeLastName(command.getLastName(), command.getOriginator());
		});
	}

	public void handle(ChangeCustomerEmailCommand command) {

		Contract.notNull(command.getEmail(), "command.getEmail()");

		action(command, customer -> {
			isTrue(repository.isEmailUnique(customer.id(), command.getEmail()), "Email " + command.getEmail() + " already occupied");
			customer.changeEmail(command.getEmail(), command.getOriginator());
		});
	}

	public void handle(DeleteCustomerCommand command) {

		action(command, customer -> customer.delete(command.getOriginator()));
	}

	// TODO: [pbilewic] 08.10.17 void updateWithSimpleConflictResolution

}
