package pl.biltec.yaess.loyalty.infrastructure;

import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.core.adapters.store.memory.InMemoryEventStore;
import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class InMemoryEventStoreTest {

	private static final Logger logger = LoggerFactory.getLogger(InMemoryEventStoreTest.class);

	private RootAggregateId RootAggregateId;
	private InMemoryEventStore store;
//	private EventStore store;

	@Before
	public void setUp() throws Exception {

		RootAggregateId = new RootAggregateId(UUID.randomUUID());
		store = new InMemoryEventStore();

	}

	@Test
	public void shouldFindNoEventsForNotExistingCustomer() throws Exception {
		//when
		List<Event> customerEvents = (List<Event>) store.loadEvents(new RootAggregateId(UUID.randomUUID()).toString(), Customer.class.getSimpleName());

		//then
		Assertions.assertThat(customerEvents).isNotNull();
		Assertions.assertThat(customerEvents).isEmpty();
	}

//	@Test
//	public void shouldFindOneEvent() throws Exception {
//		//given
//		List<Event> inputEvents = Arrays.asList(
//			new CustomerCreatedEvent(RootAggregateId, "startowy", LocalDateTime.now())
//		);
//
//		store.appendEvents(RootAggregateId, inputEvents, 0);
//
//		//when
//		List<Event> customerEvents = store.loadEvents(RootAggregateId);
//
//		//then
//		Assertions.assertThat(customerEvents).isNotNull();
//		Assertions.assertThat(customerEvents).hasSize(1);
//	}
//
//	@Test
//	public void shouldFindManyEvents() throws Exception {
//		//given
//		List<Event> inputEvents = Arrays.asList(
//			new CustomerCreatedEvent(RootAggregateId, "startowy", LocalDateTime.now()),
//			new CustomerRenamedEvent(RootAggregateId, "zmiana1", LocalDateTime.now()),
//			new CustomerRenamedEvent(RootAggregateId, "zmiana2", LocalDateTime.now()),
//			new CustomerDeletedEvent(RootAggregateId, LocalDateTime.now())
//		);
//
//		store.appendEvents(RootAggregateId, inputEvents, 0);
//
//		//when
//		List<Event> customerEvents = store.loadEvents(RootAggregateId);
//
//		//then
//		Assertions.assertThat(customerEvents).isNotNull();
//		Assertions.assertThat(customerEvents).hasSize(4);
//	}
//
//	@Test
//	public void shouldFindPartOfEventsEvents() throws Exception {
//		//given
//		CustomerRenamedEvent expectedToBeFound = new CustomerRenamedEvent(RootAggregateId, "zmiana2", LocalDateTime.now());
//		List<Event> inputEvents = Arrays.asList(
//			new CustomerCreatedEvent(RootAggregateId, "startowy", LocalDateTime.now()),
//			new CustomerRenamedEvent(RootAggregateId, "zmiana1", LocalDateTime.now()),
//			expectedToBeFound,
//			new CustomerDeletedEvent(RootAggregateId, LocalDateTime.now())
//		);
//
//		store.appendEvents(RootAggregateId, inputEvents, 0);
//
//		//when
//		List<Event> customerEvents = store.loadEvents(RootAggregateId, 2, 1);
//
//		//then
//		Assertions.assertThat(customerEvents).isNotNull();
//		Assertions.assertThat(customerEvents).hasSize(1);
//		Assertions.assertThat(customerEvents).containsExactly(expectedToBeFound);
//	}
//
//	@Test
//	public void shouldNotAllowConcurrentModification() throws Exception {
//		//given
//		List<Event> initialEvents = Arrays.asList(
//			new CustomerCreatedEvent(RootAggregateId, "startowy", LocalDateTime.now()),
//			new CustomerRenamedEvent(RootAggregateId, "zmiana1", LocalDateTime.now()),
//			new CustomerRenamedEvent(RootAggregateId, "zmiana2", LocalDateTime.now())
//		);
//		List<Event> anotherEvents = Arrays.asList(
//			new CustomerDeletedEvent(RootAggregateId, LocalDateTime.now())
//		);
//
//		store.appendEvents(RootAggregateId, initialEvents, 0);
//		List<Event> customerEvents1 = store.loadEvents(RootAggregateId);
//		List<Event> customerEvents2 = store.loadEvents(RootAggregateId);
//
//		//when
//		store.appendEvents(RootAggregateId, anotherEvents, customerEvents1.size() + anotherEvents.size());
//		try {
//			store.appendEvents(RootAggregateId, anotherEvents, customerEvents2.size());
//			Fail.fail("Exception expected");
//		}
//		catch (Exception e) {
//			Assertions.assertThat(e)
//				.isInstanceOf(ConcurrentModificationException.class)
//				.hasMessageContaining(RootAggregateId.toString());
//		}
//	}
//
//	@Test
//	@Ignore("Manual test due to async call verification")
//	public void shouldInvokeSubscribedObjectsAsync() throws Exception {
//		//given
//		List<Event> events = Arrays.asList(
//			new CustomerCreatedEvent(RootAggregateId, "startowy", LocalDateTime.now()),
//			new CustomerRenamedEvent(RootAggregateId, "zmiana1", LocalDateTime.now()),
//			new CustomerRenamedEvent(RootAggregateId, "zmiana2", LocalDateTime.now()),
//			new CustomerDeletedEvent(RootAggregateId, LocalDateTime.now())
//		);
//
//		store.addEventSubscriber(new CustomerRenamedEventLogger());
//		store.addEventSubscriber(new CustomerRenamedEventLogger2());
//		store.addEventSubscriber(new AllCustomerEventsLogger());
//		store.addEventSubscriber(new CustomerDeletedEmailSender());
//
//		//when
//		store.appendEvents(RootAggregateId, events, 0);
//
//		//then
//		logger.info("Finished");
//		Thread.sleep(1000);
//
//	}
}