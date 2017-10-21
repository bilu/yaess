package pl.biltec.yaess.clp.domain.customer;

import java.util.ConcurrentModificationException;


public interface CustomerRepository {

	Customer get(CustomerId id);

	default Customer get(CustomerId id, long concurrencyVersion) {

		Customer order = get(id);
		if (order.concurrencyVersion() != concurrencyVersion) {
			throw new ConcurrentModificationException(id.toString());
		}
		return order;
	}

	void save(Customer customer);

	boolean exists(CustomerId id);
}
