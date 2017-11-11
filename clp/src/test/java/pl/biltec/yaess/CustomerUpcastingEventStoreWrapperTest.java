package pl.biltec.yaess;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.event.CustomerChangedEmailEvent;
import pl.biltec.yaess.clp.domain.event.CustomerCreatedEvent;
import pl.biltec.yaess.clp.domain.event.CustomerDeprecatedEventsUpcaster;
import pl.biltec.yaess.clp.domain.event.CustomerRenamedEvent;
import pl.biltec.yaess.core.adapters.store.EventStore;
import pl.biltec.yaess.core.adapters.store.UpcastingEventStoreWrapper;
import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerUpcastingEventStoreWrapperTest {

	private EventStore eventStoreMock;
	private UpcastingEventStoreWrapper upcastingEventStoreWrapper;

	@Before
	public void setUp() throws Exception {

		eventStoreMock = Mockito.mock(EventStore.class);
		upcastingEventStoreWrapper = new UpcastingEventStoreWrapper(eventStoreMock, new CustomerDeprecatedEventsUpcaster());

	}

	@Test
	public void shouldUpcastingWorksAsExpected() throws Exception {
		//given
		RootAggregateId customerId = new RootAggregateId(UUID.randomUUID().toString());
		List<Event> events = Arrays.asList(
			new CustomerCreatedEvent(customerId, "name", "email@email.pl", "123", LocalDateTime.now(), "admin"),
			new CustomerChangedEmailEvent(customerId, "new@email.pl", LocalDateTime.now(), "admin2"),
			new CustomerChangedEmailEvent(customerId, "new2@email.pl", LocalDateTime.now(), "admin2"),
			new CustomerRenamedEvent(customerId, "john", LocalDateTime.now(), "admin2")
		);
		Mockito.when(eventStoreMock.loadEvents(customerId, Customer.class)).thenReturn(events);

		//when
		List<Event> upcastedEvents = upcastingEventStoreWrapper.loadEvents(customerId, Customer.class);

		//then
		Assertions.assertThat(upcastedEvents).hasSize(4);
		upcastedEvents.stream().forEach(
			event -> Assertions.assertThat(event.getClass()).isNotEqualTo(CustomerChangedEmailEvent.class)
		);
	}
}
