package pl.biltec.yaess.clp.adapters.store;

import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.customer.CustomerId;
import pl.biltec.yaess.clp.domain.customer.CustomerRepository;


public class CustomerEventStoreRepository implements CustomerRepository {

	@Override
	public Customer getById(CustomerId id) {

		return null;
	}

	@Override
	public void save(Customer customer) {

	}
}
