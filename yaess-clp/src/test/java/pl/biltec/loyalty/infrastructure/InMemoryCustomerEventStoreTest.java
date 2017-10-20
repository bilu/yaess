package pl.biltec.loyalty.infrastructure;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import org.junit.Ignore;
import org.junit.Test;

import pl.biltec.yaess.clp.adapters.SystemOutAllEventsEventSubscriberExtended;
import pl.biltec.yaess.clp.adapters.SystemOutCustomerDeletedEmailSender;
import pl.biltec.yaess.clp.adapters.SystemOutCustomerRenamedEventSubscriber;
import pl.biltec.yaess.clp.adapters.SystemOutCustomerRenamedEventSubscriber2;
import pl.biltec.yaess.clp.adapters.store.InMemoryCustomerEventStore;
import pl.biltec.yaess.clp.domain.customer.CustomerEventStore;
import pl.biltec.yaess.clp.domain.customer.CustomerEventsStream;
import pl.biltec.yaess.clp.domain.customer.CustomerId;
import pl.biltec.yaess.clp.domain.customer.event.CustomerCreatedEvent;
import pl.biltec.yaess.clp.domain.customer.event.CustomerDeletedEvent;
import pl.biltec.yaess.clp.domain.customer.event.CustomerEvent;
import pl.biltec.yaess.clp.domain.customer.event.CustomerRenamedEvent;
import pl.biltec.yaess.clp.domain.customer.exception.ConcurrentModificationException;


public class InMemoryCustomerEventStoreTest {

	@Test
	public void shouldFindNoEventsForNotExistingCustomer() throws Exception {
		//given
		CustomerEventStore store = new InMemoryCustomerEventStore();

		//when
		CustomerEventsStream customerEventsStream = store.loadEvents(new CustomerId(UUID.randomUUID()));

		//then
		Assertions.assertThat(customerEventsStream).isNotNull();
		Assertions.assertThat(customerEventsStream.getConcurrencyVersion()).isEqualTo(0);
		Assertions.assertThat(customerEventsStream.getEvents()).isEmpty();
	}

	@Test
	public void shouldFindOneEvent() throws Exception {
		//given
		CustomerId customerId = new CustomerId(UUID.randomUUID());
		CustomerEventStore store = new InMemoryCustomerEventStore();
		List<CustomerEvent> customerEvents = Arrays.asList(
			new CustomerCreatedEvent(customerId, "startowy", LocalDateTime.now())
		);

		store.appendEvents(customerId, customerEvents, 0);

		//when
		CustomerEventsStream customerEventsStream = store.loadEvents(customerId);

		//then
		Assertions.assertThat(customerEventsStream).isNotNull();
		Assertions.assertThat(customerEventsStream.getConcurrencyVersion()).isEqualTo(1);
		Assertions.assertThat(customerEventsStream.getEvents()).hasSize(1);
	}

	@Test
	public void shouldFindManyEvents() throws Exception {
		//given
		CustomerId customerId = new CustomerId(UUID.randomUUID());
		CustomerEventStore store = new InMemoryCustomerEventStore();
		List<CustomerEvent> customerEvents = Arrays.asList(
			new CustomerCreatedEvent(customerId, "startowy", LocalDateTime.now()),
			new CustomerRenamedEvent(customerId, "zmiana1", LocalDateTime.now()),
			new CustomerRenamedEvent(customerId, "zmiana2", LocalDateTime.now()),
			new CustomerDeletedEvent(customerId, LocalDateTime.now())
		);

		store.appendEvents(customerId, customerEvents, 0);

		//when
		CustomerEventsStream customerEventsStream = store.loadEvents(customerId);

		//then
		Assertions.assertThat(customerEventsStream).isNotNull();
		Assertions.assertThat(customerEventsStream.getConcurrencyVersion()).isEqualTo(4);
		Assertions.assertThat(customerEventsStream.getEvents()).hasSize(4);
	}

	@Test
	public void shouldFindPartOfEventsEvents() throws Exception {
		//given
		CustomerId customerId = new CustomerId(UUID.randomUUID());
		CustomerEventStore store = new InMemoryCustomerEventStore();
		List<CustomerEvent> customerEvents = Arrays.asList(
			new CustomerCreatedEvent(customerId, "startowy", LocalDateTime.now()),
			new CustomerRenamedEvent(customerId, "zmiana1", LocalDateTime.now()),
			new CustomerRenamedEvent(customerId, "zmiana2", LocalDateTime.now()),
			new CustomerDeletedEvent(customerId, LocalDateTime.now())
		);

		store.appendEvents(customerId, customerEvents, 0);

		//when
		CustomerEventsStream customerEventsStream = store.loadEvents(customerId, 2, 1);

		//then
		Assertions.assertThat(customerEventsStream).isNotNull();
		Assertions.assertThat(customerEventsStream.getConcurrencyVersion()).isEqualTo(3);
		Assertions.assertThat(customerEventsStream.getEvents()).hasSize(1);
	}

	@Test
	public void shouldNotAllowConcurrentModification() throws Exception {
		//given
		CustomerId customerId = new CustomerId(UUID.randomUUID());
		CustomerEventStore store = new InMemoryCustomerEventStore();
		List<CustomerEvent> initialCustomerEvents = Arrays.asList(
			new CustomerCreatedEvent(customerId, "startowy", LocalDateTime.now()),
			new CustomerRenamedEvent(customerId, "zmiana1", LocalDateTime.now()),
			new CustomerRenamedEvent(customerId, "zmiana2", LocalDateTime.now())
		);
		List<CustomerEvent> anotherCustomerEvents = Arrays.asList(
			new CustomerDeletedEvent(customerId, LocalDateTime.now())
		);

		store.appendEvents(customerId, initialCustomerEvents, 0);
		CustomerEventsStream customerEventsStream1 = store.loadEvents(customerId);
		CustomerEventsStream customerEventsStream2 = store.loadEvents(customerId);

		//when
		store.appendEvents(customerId, anotherCustomerEvents, customerEventsStream1.getConcurrencyVersion());
		try {
			store.appendEvents(customerId, anotherCustomerEvents, customerEventsStream2.getConcurrencyVersion());
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
		CustomerId customerId = new CustomerId(UUID.randomUUID());
		CustomerEventStore store = new InMemoryCustomerEventStore();
		List<CustomerEvent> events = Arrays.asList(
			new CustomerCreatedEvent(customerId, "startowy", LocalDateTime.now()),
			new CustomerRenamedEvent(customerId, "zmiana1", LocalDateTime.now()),
			new CustomerRenamedEvent(customerId, "zmiana2", LocalDateTime.now()),
			new CustomerDeletedEvent(customerId, LocalDateTime.now())
		);

		store.addEventSubscriber(new SystemOutCustomerRenamedEventSubscriber());
		store.addEventSubscriber(new SystemOutCustomerRenamedEventSubscriber2());
		store.addEventSubscriber(new SystemOutAllEventsEventSubscriberExtended());
		store.addEventSubscriber(new SystemOutCustomerDeletedEmailSender());

		//when
		store.appendEvents(customerId, events, 0);

		//then
		System.out.println("Finished");
		Thread.sleep(1000);

	}
}