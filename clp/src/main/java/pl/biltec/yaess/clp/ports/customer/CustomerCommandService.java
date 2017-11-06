package pl.biltec.yaess.clp.ports.customer;

import static pl.biltec.yaess.core.common.Contract.isTrue;
import static pl.biltec.yaess.core.common.Contract.notNull;

import java.util.function.Consumer;

import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.customer.CustomerRepository;
import pl.biltec.yaess.clp.domain.customer.exception.CustomerAlreadyCreatedException;
import pl.biltec.yaess.clp.ports.customer.command.ChangeCustomerEmailCommand;
import pl.biltec.yaess.clp.ports.customer.command.ChangeCustomerNameCommand;
import pl.biltec.yaess.clp.ports.customer.command.CreateCustomerCommand;
import pl.biltec.yaess.clp.ports.customer.command.DeleteCustomerCommand;
import pl.biltec.yaess.core.common.Contract;
import pl.biltec.yaess.core.common.exception.ConditionNotMetException;
import pl.biltec.yaess.core.domain.RootAggregateId;


/**
 * <pre>
 * <b>Purpose:</b>
 * 	Public API / Entry point to interact with Customer domain
 * <b>Question:</b>
 * 	Do i really need Commands, why can't i use "normal, descriptive methods"?
 * </pre>
 *
 */
public class CustomerCommandService {

	private CustomerRepository customerRepository;
	private AuthorizationService authorizationService;

	public CustomerCommandService(CustomerRepository customerRepository, AuthorizationService authorizationService) {


		this.customerRepository = notNull(customerRepository, "customerRepository");
		this.authorizationService = notNull(authorizationService, "authorizationService");
	}

	public void handle(CreateCustomerCommand command) {

		notNull(command, "command");
		checkAuthorization(command);
		isTrue(customerRepository.isEmailUnique(command.getEmail()), "Email " + command.getEmail() + " already occupied");
		Customer customer = new Customer(command.rootAggregateId, command.getName(), command.getEmail(), command.getPersonalIdNumber(), command.getOriginator());
		// TODO [bilu] 28.10.17  unify type of exception ContractBroken vs DomainOperation
		if (customerRepository.exists(customer.id())) {
			throw new CustomerAlreadyCreatedException(customer.id());
		}
		customerRepository.save(customer);
	}

	public void handle(ChangeCustomerNameCommand command) {

		action(command, customer -> customer.rename(command.getName(), command.originator));
	}

	public void handle(ChangeCustomerEmailCommand command) {

		Contract.notNull(command.getEmail(), "command.getEmail()");

		action(command, customer -> {
			isTrue(customerRepository.isEmailUnique(customer.id(), command.getEmail()), "Email " + command.getEmail() + " already occupied");
			customer.changeEmail(command.getEmail(), command.getOriginator());
		});
	}

	public void handle(DeleteCustomerCommand command) {

		action(command, customer -> customer.delete(command.getOriginator()));
	}

	void action(Command command, Consumer<Customer> action) {

		notNull(command, "command");
		checkAuthorization(command);
		Customer customer = customerRepository.get(new RootAggregateId(command.rootAggregateId));
		action.accept(customer);
		customerRepository.save(customer);
	}

	private void checkAuthorization(Command command) {

		Contract.isTrue(
			authorizationService.isAllowedToInvokeCommand(command),
			() -> new ConditionNotMetException("User " + command.originator + " is not allowed to perform " + command));
	}

	// TODO: [pbilewic] 08.10.17 void updateWithSimpleConflictResolution

}
