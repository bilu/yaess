package pl.biltec.yaess.clp.ports.customer;

import java.util.function.Consumer;

import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.customer.CustomerId;
import pl.biltec.yaess.clp.domain.customer.CustomerRepository;
import pl.biltec.yaess.clp.domain.customer.exception.CustomerAlreadyCreatedException;
import pl.biltec.yaess.clp.ports.customer.command.CreateCustomerCommand;
import pl.biltec.yaess.clp.ports.customer.command.CustomerCommand;
import pl.biltec.yaess.clp.ports.customer.command.DeleteCustomerCommand;
import pl.biltec.yaess.clp.ports.customer.command.RenameCustomerCommand;
import pl.biltec.yaess.clp.ports.customer.exception.UnsupportedCommandException;
import pl.biltec.yaess.core.common.Contract;


public class CustomerApplicationService {

	private CustomerRepository customerRepository;

	public CustomerApplicationService(CustomerRepository customerRepository) {

		Contract.notNull(customerRepository, "customerRepository");
		this.customerRepository = customerRepository;
	}

	public void execute(CustomerCommand command) {

		throw new UnsupportedCommandException(command);
	}

	public String execute(CreateCustomerCommand command) {

		Customer customer = new Customer(command.getName());
		if (customerRepository.exists(customer.id())) {
			throw new CustomerAlreadyCreatedException(customer.id());
		}
		customerRepository.save(customer);
		return customer.id().toString();
	}

	public void execute(RenameCustomerCommand command) {

		update(command.getId(), customer -> customer.rename(command.getNewName()));
	}

	public void execute(DeleteCustomerCommand command) {

		update(command.getId(), customer -> customer.delete());
	}

	void update(CustomerId customerId, Consumer<Customer> action) {

		Customer customer = customerRepository.get(customerId);
		action.accept(customer);
		customerRepository.save(customer);
	}

	// TODO: [pbilewic] 08.10.17 void updateWithSimpleConflictResolution


}
