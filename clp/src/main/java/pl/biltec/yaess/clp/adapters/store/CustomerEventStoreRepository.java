package pl.biltec.yaess.clp.adapters.store;

import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.customer.CustomerId;
import pl.biltec.yaess.clp.domain.customer.CustomerRepository;
import pl.biltec.yaess.clp.domain.customer.event.CustomerEvent;
import pl.biltec.yaess.core.adapters.store.EventStore;


public class CustomerEventStoreRepository implements CustomerRepository {

	private EventStore<CustomerId, CustomerEvent> eventStore;

	public CustomerEventStoreRepository(EventStore eventStore) {

		this.eventStore = eventStore;
	}

	@Override
	public Customer get(CustomerId id) {
		return new Customer(eventStore.loadEvents(id));
	}

	@Override
	public void save(Customer customer) {

		eventStore.appendEvents(customer.id(), customer.getUncommittedEvents(), customer.concurrencyVersion());
		customer.clearUncommittedEvents();
	}

	@Override
	public boolean exists(CustomerId id) {

		return eventStore.exists(id);
	}
}
