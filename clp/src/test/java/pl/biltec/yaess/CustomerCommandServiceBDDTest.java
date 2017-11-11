package pl.biltec.yaess;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.junit.Test;

import pl.biltec.yaess.clp.adapters.store.CustomerRepositoryOverEventStore;
import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.event.CustomerChangedEmailEvent;
import pl.biltec.yaess.clp.domain.event.CustomerChangedEmailV2Event;
import pl.biltec.yaess.clp.domain.event.CustomerCreatedEvent;
import pl.biltec.yaess.clp.domain.event.CustomerDeprecatedEventsUpcaster;
import pl.biltec.yaess.clp.ports.customer.AuthorizationService;
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

		Map<Class<? extends Event>, Function<Event, Event>> upcaster = new HashMap<>();
		upcaster.put(CustomerChangedEmailEvent.class, new Function<Event, Event>() {
			@Override
			public Event apply(Event event) {
				CustomerChangedEmailEvent oldEvent = (CustomerChangedEmailEvent) event;
				return new CustomerChangedEmailV2Event(oldEvent.rootAggregateId(), "unknown", oldEvent.getEmail(), oldEvent.created(), oldEvent.originator());
			}
		});
		eventStore = new UpcastingEventStoreWrapper(eventStore, new CustomerDeprecatedEventsUpcaster());
		customerRepository = new CustomerRepositoryOverEventStore(eventStore, snapshotStore, uniqueValueStore, Customer.class);
		AuthorizationService allowEveryoneAuthorizationService = command -> true;
		return new CustomerCommandService(customerRepository, allowEveryoneAuthorizationService);
	}

	private SingleEventSubscriber<CustomerChangedEmailV2Event> uniqueEmailsRemover(UniqueValuesStore uniqueValueStore) {

		return new SingleEventSubscriber<CustomerChangedEmailV2Event>(CustomerChangedEmailV2Event.class) {

			@Override
			public void handle(CustomerChangedEmailV2Event event) {

				uniqueValueStore.removeUnique(Customer.class, event.rootAggregateId(), CustomerRepositoryOverEventStore.EMAIL_ATTRIBUTE_NAME, event.getOldEmail());

			}
		};
	}

	private SingleEventSubscriber<Event> uniqueEmailsAppender(UniqueValuesStore uniqueValueStore) {

		return new SingleEventSubscriber<Event>(Event.class) {

			@Override
			public void handle(Event event) {

				if(event instanceof CustomerChangedEmailV2Event) {
					CustomerChangedEmailV2Event e = (CustomerChangedEmailV2Event) event;
					uniqueValueStore.addUnique(Customer.class, event.rootAggregateId(), CustomerRepositoryOverEventStore.EMAIL_ATTRIBUTE_NAME, e.getNewEmail());
				} else if(event instanceof CustomerCreatedEvent) {
					CustomerCreatedEvent e = (CustomerCreatedEvent) event;
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
			new CreateCustomerCommand(customerId1, "a", "b", "c@email.pl", "d"),
			new ChangeCustomerEmailCommand(customerId1, "b", "d@email.pl")
		);

		whenWaitForMillis(10).andWhen(
			new CreateCustomerCommand(customerId2, "a", "b", "d@email.pl", "d")
		);

		thenThrow(ConditionNotMetException.class, "d@email.pl already occupied");

	}

}
