package pl.biltec.yaess.clp.adapters.store;

import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.customer.CustomerId;
import pl.biltec.yaess.clp.domain.customer.CustomerRepository;
import pl.biltec.yaess.clp.domain.customer.event.CustomerEvent;
import pl.biltec.yaess.core.adapters.store.EventStore;
import pl.biltec.yaess.core.adapters.store.EventStoreWrapperRepository;


public class CustomerEventStoreWrapperRepository
	extends EventStoreWrapperRepository<CustomerId, CustomerEvent, Customer>
	implements CustomerRepository {

	public CustomerEventStoreWrapperRepository(EventStore<CustomerId, CustomerEvent> eventStore) {

		super(eventStore, Customer.class);
	}
}
