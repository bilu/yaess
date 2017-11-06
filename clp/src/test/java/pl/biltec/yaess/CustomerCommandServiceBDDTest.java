package pl.biltec.yaess;

import org.junit.Test;

import pl.biltec.yaess.clp.adapters.store.CustomerRepositoryOverEventStore;
import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.event.CustomerChangedEmailEvent;
import pl.biltec.yaess.clp.domain.event.CustomerCreatedEvent;
import pl.biltec.yaess.clp.ports.customer.AuthorizationService;
import pl.biltec.yaess.clp.ports.customer.CustomerCommandService;
import pl.biltec.yaess.clp.ports.customer.command.ChangeCustomerEmailCommand;
import pl.biltec.yaess.clp.ports.customer.command.CreateCustomerCommand;
import pl.biltec.yaess.core.adapters.store.EventStore;
import pl.biltec.yaess.core.adapters.store.SingleEventSubscriber;
import pl.biltec.yaess.core.adapters.store.SnapshotStore;
import pl.biltec.yaess.core.adapters.store.UniqueValuesStore;
import pl.biltec.yaess.core.adapters.store.memory.InMemoryEventStore;
import pl.biltec.yaess.core.adapters.store.memory.InMemorySnapshotStore;
import pl.biltec.yaess.core.adapters.store.memory.InMemoryUniqueValuesStore;
import pl.biltec.yaess.core.common.exception.ConditionNotMetException;
import pl.biltec.yaess.core.domain.Event;


public class CustomerCommandServiceBDDTest extends BDDTest<CustomerCommandServiceBDDTest, CustomerCommandService> {

	@Override
	public CustomerCommandService prepare() {

		EventStore eventStore = new InMemoryEventStore();
		SnapshotStore snapshotStore = new InMemorySnapshotStore();
		UniqueValuesStore uniqueValueStore = new InMemoryUniqueValuesStore();
		eventStore.addEventSubscriber(uniqueEmailsUpdater(uniqueValueStore));
		CustomerRepositoryOverEventStore customerRepository;
		customerRepository = new CustomerRepositoryOverEventStore(eventStore, snapshotStore, uniqueValueStore, Customer.class);
		AuthorizationService allowEveryoneAuthorizationService = command -> true;
		return new CustomerCommandService(customerRepository, allowEveryoneAuthorizationService);
	}

	private SingleEventSubscriber<Event> uniqueEmailsUpdater(UniqueValuesStore uniqueValueStore) {

		return new SingleEventSubscriber<Event>(Event.class) {

			@Override
			public void handle(Event event) {

				if(event instanceof CustomerChangedEmailEvent) {
					CustomerChangedEmailEvent e = (CustomerChangedEmailEvent) event;
					uniqueValueStore.addUnique(Customer.class, event.rootAggregateId(), "EMAIL", e.getEmail());
				} else if(event instanceof CustomerCreatedEvent) {
					CustomerCreatedEvent e = (CustomerCreatedEvent) event;
					uniqueValueStore.addUnique(Customer.class, event.rootAggregateId(), "EMAIL", e.getEmail());
				}
			}
		};
	}

	@Test
	public void shouldBDDTestWorkFine() throws Exception {

		String customerId1 = givenRandomId();
		String customerId2 = givenRandomId();
		given(
			new CreateCustomerCommand(customerId1, "a", "b", "c", "d"),
			new ChangeCustomerEmailCommand(customerId1, "b", "d")
		);

		when(
			new CreateCustomerCommand(customerId2, "a", "b", "c", "d")
		);

		thenThrow(ConditionNotMetException.class, "c already occupied");
	}

}
