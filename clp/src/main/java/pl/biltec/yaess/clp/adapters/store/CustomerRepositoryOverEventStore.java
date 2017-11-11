package pl.biltec.yaess.clp.adapters.store;

import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.customer.CustomerRepository;
import pl.biltec.yaess.core.adapters.store.EventStore;
import pl.biltec.yaess.core.adapters.store.NoEventUpcaster;
import pl.biltec.yaess.core.adapters.store.RepositoryOverEventStore;
import pl.biltec.yaess.core.adapters.store.SnapshotStore;
import pl.biltec.yaess.core.adapters.store.UniqueValuesStore;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerRepositoryOverEventStore extends RepositoryOverEventStore<Customer> implements CustomerRepository {

	public static final String EMAIL_ATTRIBUTE_NAME = "EMAIL";

	public CustomerRepositoryOverEventStore(
		EventStore eventStore,
		Class<Customer> customerClass) {

		super(eventStore, customerClass);
	}

	public CustomerRepositoryOverEventStore(
		EventStore eventStore,
		SnapshotStore snapshotStore,
		UniqueValuesStore uniqueValuesStore,
		Class<Customer> customerClass) {

		super(eventStore, new NoEventUpcaster(), snapshotStore, uniqueValuesStore, customerClass);
	}

	@Override
	public boolean isEmailUnique(String email) {

		return isUnique(EMAIL_ATTRIBUTE_NAME, email);
	}

	@Override
	public boolean isEmailUnique(RootAggregateId rootAggregateId, String email) {

		return isUnique(rootAggregateId, EMAIL_ATTRIBUTE_NAME, email);
	}

}
