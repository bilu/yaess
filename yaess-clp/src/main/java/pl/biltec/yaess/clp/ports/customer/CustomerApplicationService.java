package pl.biltec.yaess.clp.ports.customer;

import java.util.function.Consumer;

import pl.biltec.yaess.clp.ports.customer.command.CreateCustomerCommand;
import pl.biltec.yaess.clp.ports.customer.command.CustomerCommand;
import pl.biltec.yaess.clp.ports.customer.command.DeleteCustomerCommand;
import pl.biltec.yaess.clp.ports.customer.command.RenameCustomerCommand;
import pl.biltec.yaess.clp.ports.customer.command.exception.UnsupportedCommandException;
import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.customer.CustomerEventStore;
import pl.biltec.yaess.clp.domain.customer.CustomerEventsStream;
import pl.biltec.yaess.clp.domain.customer.CustomerId;
import pl.biltec.yaess.clp.domain.customer.exception.CustomerAlreadyCreatedException;


public class CustomerApplicationService {

	private CustomerEventStore customerEventStore;

	public CustomerApplicationService(CustomerEventStore customerEventStore) {

		this.customerEventStore = customerEventStore;
	}

	public void execute(CustomerCommand command) {

		throw new UnsupportedCommandException(command);
	}

	public String execute(CreateCustomerCommand command) {

		Customer customer = new Customer(command.getName());
		if (customerEventStore.alreadyExists(customer.getId())) {
			throw new CustomerAlreadyCreatedException(customer.getId());
		}
		// TODO: [pbilewic] 12.10.17 static 0 convert 
		customerEventStore.appendEvents(customer.getId(), customer.getUncommittedEvents(), 0);
		return customer.getId().toString();
	}

	public void execute(RenameCustomerCommand command) {

		update(command.getId(), customer -> customer.rename(command.getNewName()));
	}

	public void execute(DeleteCustomerCommand command) {

		update(command.getId(), customer -> customer.delete());
	}

	void update(CustomerId customerId, Consumer<Customer> action) {

		CustomerEventsStream customerEventsStream = customerEventStore.loadEvents(customerId);
		Customer customer = new Customer(customerEventsStream.getEvents());
		action.accept(customer);
		customerEventStore.appendEvents(customerId, customer.getUncommittedEvents(), customerEventsStream.getConcurrencyVersion());
	}

	// TODO: [pbilewic] 08.10.17 void updateWithSimpleConflictResolution


}
