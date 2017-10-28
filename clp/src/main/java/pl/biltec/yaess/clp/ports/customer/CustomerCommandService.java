package pl.biltec.yaess.clp.ports.customer;

import static pl.biltec.yaess.core.common.Contract.isTrue;
import static pl.biltec.yaess.core.common.Contract.notNull;

import java.util.function.Consumer;

import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.customer.CustomerRepository;
import pl.biltec.yaess.clp.domain.customer.exception.CustomerAlreadyCreatedException;
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

	public CustomerCommandService(CustomerRepository customerRepository) {

		notNull(customerRepository, "customerRepository");
		this.customerRepository = customerRepository;
	}

	public String createCustomer(String customerName, String email) {

		notNull(customerName, "customerName");
		isTrue(customerRepository.isEmailUnique(email), "Email " + email + " already occupied");
		Customer customer = new Customer(customerName, email);
		// TODO [bilu] 28.10.17  unify type of exceptionc ContractBroken vs DomainOperation
		if (customerRepository.exists(customer.id())) {
			throw new CustomerAlreadyCreatedException(customer.id());
		}
		customerRepository.save(customer);
		return customer.id().toString();
	}

	public void rename(String customerId, String newName) {

		notNull(newName, "newName");
		action(customerId, customer -> customer.rename(newName));
	}

	public void delete(String customerId) {

		action(customerId, customer -> customer.delete());
	}

	void action(String customerId, Consumer<Customer> action) {

		notNull(customerId, "customerId");
		Customer customer = customerRepository.get(new RootAggregateId(customerId));
		action.accept(customer);
		customerRepository.save(customer);
	}

	// TODO: [pbilewic] 08.10.17 void updateWithSimpleConflictResolution

}
