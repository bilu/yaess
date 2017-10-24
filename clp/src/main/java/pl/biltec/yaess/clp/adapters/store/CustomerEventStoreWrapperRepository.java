package pl.biltec.yaess.clp.adapters.store;

import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.customer.CustomerRepository;
import pl.biltec.yaess.core.adapters.store.EventStore;
import pl.biltec.yaess.core.adapters.store.EventStoreWrapperRepository;
import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerEventStoreWrapperRepository
	extends EventStoreWrapperRepository<RootAggregateId, Event, Customer>
	implements CustomerRepository {

	public CustomerEventStoreWrapperRepository(EventStore<RootAggregateId, Event> eventStore) {

		super(eventStore, Customer.class);
	}
}
