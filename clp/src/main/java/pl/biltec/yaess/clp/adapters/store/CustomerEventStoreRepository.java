package pl.biltec.yaess.clp.adapters.store;

import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.customer.CustomerEventStore;
import pl.biltec.yaess.clp.domain.customer.CustomerId;
import pl.biltec.yaess.clp.domain.customer.CustomerRepository;


public class CustomerEventStoreRepository implements CustomerRepository {

	private CustomerEventStore customerEventStore;

	public CustomerEventStoreRepository(CustomerEventStore customerEventStore) {

		this.customerEventStore = customerEventStore;
	}

	@Override
	public Customer get(CustomerId id) {
		return new Customer(customerEventStore.loadEvents(id));
	}

	@Override
	public void save(Customer customer) {

		customerEventStore.appendEvents(customer.id(), customer.getUncommittedEvents(), customer.concurrencyVersion());
		customer.clearUncommittedEvents();
	}

	@Override
	public boolean exists(CustomerId id) {

		return customerEventStore.exists(id);
	}
}
