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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.biltec.yaess.clp.adapters.AllCustomerEventsLogger;
import pl.biltec.yaess.clp.adapters.CustomerDeletedEmailSender;
import pl.biltec.yaess.clp.adapters.CustomerRenamedEventLogger;
import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.event.CustomerCreatedV2Event;
import pl.biltec.yaess.clp.domain.event.CustomerDeletedEvent;
import pl.biltec.yaess.clp.domain.event.CustomerRenamedEvent;
import pl.biltec.yaess.core.adapters.store.EventStore;
import pl.biltec.yaess.core.adapters.store.memory.InMemoryEventStore;
import pl.biltec.yaess.core.common.exception.ConcurrentModificationException;
import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class InMemoryEventStoreTest {

	private static final Logger logger = LoggerFactory.getLogger(InMemoryEventStoreTest.class);

	private RootAggregateId customerId;
	private Class<Customer> rootAggregateClass;
	private EventStore store;

	@Before
	public void setUp() throws Exception {

		customerId = new RootAggregateId(UUID.randomUUID());
		store = new InMemoryEventStore();
		rootAggregateClass = Customer.class;

	}

	@Test
	public void shouldFindNoEventsForNotExistingCustomer() throws Exception {
		//when
		List<Event> customerEvents = (List<Event>) store.loadEvents(new RootAggregateId(UUID.randomUUID()), Customer.class);

		//then
		Assertions.assertThat(customerEvents).isNotNull();
		Assertions.assertThat(customerEvents).isEmpty();
	}

	@Test
	public void shouldFindOneEvent() throws Exception {
		//given
		List<Event> inputEvents = Arrays.asList(
			new CustomerCreatedV2Event(customerId, "john", "Doe", "test@email.pl", "80112233445", LocalDateTime.now(), "admin")
		);

		store.appendEvents(customerId, rootAggregateClass, inputEvents, 0);

		//when
		List<Event> customerEvents = store.loadEvents(customerId, rootAggregateClass);

		//then
		Assertions.assertThat(customerEvents).isNotNull();
		Assertions.assertThat(customerEvents).hasSize(1);
	}

	@Test
	public void shouldFindManyEvents() throws Exception {
		//given
		List<Event> inputEvents = Arrays.asList(
			new CustomerCreatedV2Event(customerId, "john", "Doe", "test@email.pl", "88112233445", LocalDateTime.now(), "admin"),
			new CustomerRenamedEvent(customerId, "zmiana1", LocalDateTime.now(), "admin"),
			new CustomerRenamedEvent(customerId, "zmiana2", LocalDateTime.now(), "admin"),
			new CustomerDeletedEvent(customerId, LocalDateTime.now(), "admin")
		);

		store.appendEvents(customerId, rootAggregateClass, inputEvents, 0);

		//when
		List<Event> customerEvents = store.loadEvents(customerId, rootAggregateClass);

		//then
		Assertions.assertThat(customerEvents).isNotNull();
		Assertions.assertThat(customerEvents).hasSize(4);
	}

	@Test
	public void shouldFindPartOfEventsEvents() throws Exception {
		//given
		CustomerRenamedEvent expectedToBeFound = new CustomerRenamedEvent(customerId, "zmiana2", LocalDateTime.now(), "admin");
		List<Event> inputEvents = Arrays.asList(
			new CustomerCreatedV2Event(customerId, "john", "Doe", "test@email.pl", "88112233445", LocalDateTime.now(), "admin"),
			new CustomerRenamedEvent(customerId, "zmiana1", LocalDateTime.now(), "admin"),
			expectedToBeFound,
			new CustomerDeletedEvent(customerId, LocalDateTime.now(), "admin")
		);

		store.appendEvents(customerId, rootAggregateClass, inputEvents, 0);

		//when
		List<Event> customerEvents = store.loadEvents(customerId, rootAggregateClass, 2, 1);

		//then
		Assertions.assertThat(customerEvents).isNotNull();
		Assertions.assertThat(customerEvents).hasSize(1);
		Assertions.assertThat(customerEvents).containsExactly(expectedToBeFound);
	}

	@Test
	public void shouldNotAllowConcurrentModification() throws Exception {
		//given
		List<Event> initialEvents = Arrays.asList(
			new CustomerCreatedV2Event(customerId, "john", "Doe", "test@email.pl", "88112233445", LocalDateTime.now(), "admin"),
			new CustomerRenamedEvent(customerId, "zmiana1", LocalDateTime.now(), "admin"),
			new CustomerRenamedEvent(customerId, "zmiana2", LocalDateTime.now(), "admin")
		);
		List<Event> anotherEvents = Arrays.asList(
			new CustomerDeletedEvent(customerId, LocalDateTime.now(), "admin")
		);

		store.appendEvents(customerId, rootAggregateClass, initialEvents, 0);
		List<Event> customerEvents1 = store.loadEvents(customerId, rootAggregateClass);
		List<Event> customerEvents2 = store.loadEvents(customerId, rootAggregateClass);

		//when
		store.appendEvents(customerId, rootAggregateClass, anotherEvents, customerEvents1.size() + anotherEvents.size());
		try {
			store.appendEvents(customerId, rootAggregateClass, anotherEvents, customerEvents2.size());
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
		List<Event> events = Arrays.asList(
			new CustomerCreatedV2Event(customerId, "john", "Doe", "test@email.pl", "88112233445", LocalDateTime.now(), "admin"),
			new CustomerRenamedEvent(customerId, "zmiana1", LocalDateTime.now(), "admin"),
			new CustomerRenamedEvent(customerId, "zmiana2", LocalDateTime.now(), "admin"),
			new CustomerDeletedEvent(customerId, LocalDateTime.now(), "admin")
		);

		store.addEventSubscriber(new CustomerRenamedEventLogger());
		store.addEventSubscriber(new AllCustomerEventsLogger());
		store.addEventSubscriber(new CustomerDeletedEmailSender());

		//when
		store.appendEvents(customerId, rootAggregateClass, events, 0);

		//then
		logger.info("Finished");
		Thread.sleep(1000);

	}
}