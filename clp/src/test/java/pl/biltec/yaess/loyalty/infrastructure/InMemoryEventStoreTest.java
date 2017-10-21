package pl.biltec.yaess.loyalty.infrastructure;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import pl.biltec.yaess.clp.adapters.SystemOutAllEventsEventSubscriberExtendedEventSubscriber;
import pl.biltec.yaess.clp.adapters.SystemOutDeletedEmailSender;
import pl.biltec.yaess.clp.adapters.SystemOutRenamedEventSubscriber;
import pl.biltec.yaess.clp.adapters.SystemOutRenamedEventSubscriber2;
import pl.biltec.yaess.clp.adapters.store.InMemoryEventStore;
import pl.biltec.yaess.core.adapters.store.EventStore;
import pl.biltec.yaess.clp.domain.customer.CustomerId;
import pl.biltec.yaess.clp.domain.customer.event.CustomerCreatedEvent;
import pl.biltec.yaess.clp.domain.customer.event.CustomerDeletedEvent;
import pl.biltec.yaess.clp.domain.customer.event.CustomerEvent;
import pl.biltec.yaess.clp.domain.customer.event.CustomerRenamedEvent;
import pl.biltec.yaess.clp.domain.customer.exception.ConcurrentModificationException;


public class InMemoryEventStoreTest {

	private CustomerId customerId;
	private EventStore store;

	@Before
	public void setUp() throws Exception {

		customerId = new CustomerId(UUID.randomUUID());
		store = new InMemoryEventStore();

	}

	@Test
	public void shouldFindNoEventsForNotExistingCustomer() throws Exception {
		//given
		EventStore store = new InMemoryEventStore();

		//when
		List<CustomerEvent> customerEvents = store.loadEvents(new CustomerId(UUID.randomUUID()));

		//then
		Assertions.assertThat(customerEvents).isNotNull();
		Assertions.assertThat(customerEvents).isEmpty();
	}

	@Test
	public void shouldFindOneEvent() throws Exception {
		//given
		List<CustomerEvent> inputCustomerEvents = Arrays.asList(
			new CustomerCreatedEvent(customerId, "startowy", LocalDateTime.now())
		);

		store.appendEvents(customerId, inputCustomerEvents, 0);

		//when
		List<CustomerEvent> customerEvents = store.loadEvents(customerId);

		//then
		Assertions.assertThat(customerEvents).isNotNull();
		Assertions.assertThat(customerEvents).hasSize(1);
	}

	@Test
	public void shouldFindManyEvents() throws Exception {
		//given
		List<CustomerEvent> inputCustomerEvents = Arrays.asList(
			new CustomerCreatedEvent(customerId, "startowy", LocalDateTime.now()),
			new CustomerRenamedEvent(customerId, "zmiana1", LocalDateTime.now()),
			new CustomerRenamedEvent(customerId, "zmiana2", LocalDateTime.now()),
			new CustomerDeletedEvent(customerId, LocalDateTime.now())
		);

		store.appendEvents(customerId, inputCustomerEvents, 0);

		//when
		List<CustomerEvent> customerEvents = store.loadEvents(customerId);

		//then
		Assertions.assertThat(customerEvents).isNotNull();
		Assertions.assertThat(customerEvents).hasSize(4);
	}

	@Test
	public void shouldFindPartOfEventsEvents() throws Exception {
		//given
		CustomerRenamedEvent expectedToBeFound = new CustomerRenamedEvent(customerId, "zmiana2", LocalDateTime.now());
		List<CustomerEvent> inputCustomerEvents = Arrays.asList(
			new CustomerCreatedEvent(customerId, "startowy", LocalDateTime.now()),
			new CustomerRenamedEvent(customerId, "zmiana1", LocalDateTime.now()),
			expectedToBeFound,
			new CustomerDeletedEvent(customerId, LocalDateTime.now())
		);

		store.appendEvents(customerId, inputCustomerEvents, 0);

		//when
		List<CustomerEvent> customerEvents = store.loadEvents(customerId, 2, 1);

		//then
		Assertions.assertThat(customerEvents).isNotNull();
		Assertions.assertThat(customerEvents).hasSize(1);
		Assertions.assertThat(customerEvents).containsExactly(expectedToBeFound);
	}

	@Test
	public void shouldNotAllowConcurrentModification() throws Exception {
		//given
		List<CustomerEvent> initialCustomerEvents = Arrays.asList(
			new CustomerCreatedEvent(customerId, "startowy", LocalDateTime.now()),
			new CustomerRenamedEvent(customerId, "zmiana1", LocalDateTime.now()),
			new CustomerRenamedEvent(customerId, "zmiana2", LocalDateTime.now())
		);
		List<CustomerEvent> anotherCustomerEvents = Arrays.asList(
			new CustomerDeletedEvent(customerId, LocalDateTime.now())
		);

		store.appendEvents(customerId, initialCustomerEvents, 0);
		List<CustomerEvent> customerEvents1 = store.loadEvents(customerId);
		List<CustomerEvent> customerEvents2 = store.loadEvents(customerId);

		//when
		store.appendEvents(customerId, anotherCustomerEvents, customerEvents1.size() + anotherCustomerEvents.size());
		try {
			store.appendEvents(customerId, anotherCustomerEvents, customerEvents2.size());
			Fail.fail("Exception expected");
		}
		catch (Exception e) {
			Assertions.assertThat(e)
				.isInstanceOf(ConcurrentModificationException.class)
				.hasMessageContaining(customerId.toString());
		}
	}

	@Test
	@Ignore("Manual test due to async call verification")
	public void shouldInvokeSubscribedObjectsAsync() throws Exception {
		//given
		List<CustomerEvent> events = Arrays.asList(
			new CustomerCreatedEvent(customerId, "startowy", LocalDateTime.now()),
			new CustomerRenamedEvent(customerId, "zmiana1", LocalDateTime.now()),
			new CustomerRenamedEvent(customerId, "zmiana2", LocalDateTime.now()),
			new CustomerDeletedEvent(customerId, LocalDateTime.now())
		);

		store.addEventSubscriber(new SystemOutRenamedEventSubscriber());
		store.addEventSubscriber(new SystemOutRenamedEventSubscriber2());
		store.addEventSubscriber(new SystemOutAllEventsEventSubscriberExtendedEventSubscriber());
		store.addEventSubscriber(new SystemOutDeletedEmailSender());

		//when
		store.appendEvents(customerId, events, 0);

		//then
		System.out.println("Finished");
		Thread.sleep(1000);

	}
}