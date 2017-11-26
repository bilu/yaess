package pl.biltec.yaess;

import org.junit.Test;

import pl.biltec.yaess.clp.adapters.store.CustomerRepositoryOverEventStore;
import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.event.CustomerEmailChangedV2Event;
import pl.biltec.yaess.clp.domain.event.CustomerCreatedV2Event;
import pl.biltec.yaess.clp.domain.event.CustomerDeprecatedEventsUpcaster;
import pl.biltec.yaess.clp.ports.AuthorizationService;
import pl.biltec.yaess.clp.ports.customer.CustomerCommandService;
import pl.biltec.yaess.clp.ports.customer.command.ChangeCustomerEmailCommand;
import pl.biltec.yaess.clp.ports.customer.command.CreateCustomerCommand;
import pl.biltec.yaess.core.adapters.store.EventStore;
import pl.biltec.yaess.core.adapters.store.SingleEventSubscriber;
import pl.biltec.yaess.core.adapters.store.SnapshotStore;
import pl.biltec.yaess.core.adapters.store.UniqueValuesStore;
import pl.biltec.yaess.core.adapters.store.UpcastingEventStoreWrapper;
import pl.biltec.yaess.core.adapters.store.memory.InMemoryEventStore;
import pl.biltec.yaess.core.adapters.store.memory.InMemorySnapshotStore;
import pl.biltec.yaess.core.adapters.store.memory.InMemoryUniqueValuesStore;
import pl.biltec.yaess.core.common.exception.ConditionNotMetException;
import pl.biltec.yaess.core.domain.Event;


public class CustomerCommandServiceBDDTest extends BDDTest<CustomerCommandServiceBDDTest, CustomerCommandService> {

	private CustomerRepositoryOverEventStore customerRepository;

	@Override
	public CustomerCommandService prepare() {

		EventStore eventStore = new InMemoryEventStore();
		SnapshotStore snapshotStore = new InMemorySnapshotStore();
		UniqueValuesStore uniqueValueStore = new InMemoryUniqueValuesStore();
		eventStore.addEventSubscriber(uniqueEmailsAppender(uniqueValueStore));
		eventStore.addEventSubscriber(uniqueEmailsRemover(uniqueValueStore));

		eventStore = new UpcastingEventStoreWrapper(eventStore, new CustomerDeprecatedEventsUpcaster());
		customerRepository = new CustomerRepositoryOverEventStore(eventStore, snapshotStore, uniqueValueStore, Customer.class);
		AuthorizationService allowEveryoneAuthorizationService = command -> true;
		return new CustomerCommandService(customerRepository, allowEveryoneAuthorizationService);
	}

	private SingleEventSubscriber<CustomerEmailChangedV2Event> uniqueEmailsRemover(UniqueValuesStore uniqueValueStore) {

		return new SingleEventSubscriber<CustomerEmailChangedV2Event>(CustomerEmailChangedV2Event.class) {

			@Override
			public void handle(CustomerEmailChangedV2Event event) {

				uniqueValueStore.removeUnique(Customer.class, event.rootAggregateId(), CustomerRepositoryOverEventStore.EMAIL_ATTRIBUTE_NAME, event.getOldEmail());
			}
		};
	}

	private SingleEventSubscriber<Event> uniqueEmailsAppender(UniqueValuesStore uniqueValueStore) {

		return new SingleEventSubscriber<Event>(Event.class) {

			@Override
			public void handle(Event event) {

				if (event instanceof CustomerEmailChangedV2Event) {
					CustomerEmailChangedV2Event e = (CustomerEmailChangedV2Event) event;
					uniqueValueStore.addUnique(Customer.class, event.rootAggregateId(), CustomerRepositoryOverEventStore.EMAIL_ATTRIBUTE_NAME, e.getNewEmail());
				}
				else if (event instanceof CustomerCreatedV2Event) {
					CustomerCreatedV2Event e = (CustomerCreatedV2Event) event;
					uniqueValueStore.addUnique(Customer.class, event.rootAggregateId(), CustomerRepositoryOverEventStore.EMAIL_ATTRIBUTE_NAME, e.getEmail());
				}
			}
		};
	}

	@Test
	public void shouldNotAllowToCreateTheSameEmailForTwoCustomers() throws Exception {

		String customerId1 = givenRandomId();
		String customerId2 = givenRandomId();
		given(
			new CreateCustomerCommand(customerId1, "a", "b", "surname", "c@email.pl", "d"),
			new ChangeCustomerEmailCommand(customerId1, "b", "d@email.pl")
		);

		whenWaitForMillis(10).andWhen(
			new CreateCustomerCommand(customerId2, "a", "b", "surname", "d@email.pl", "d")
		);

		thenThrow(ConditionNotMetException.class, "d@email.pl already occupied");

	}

}
