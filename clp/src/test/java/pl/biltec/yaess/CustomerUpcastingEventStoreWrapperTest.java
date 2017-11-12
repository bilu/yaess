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
import pl.biltec.yaess.clp.domain.event.CustomerEmailChangedEvent;
import pl.biltec.yaess.clp.domain.event.CustomerCreatedV2Event;
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
			new CustomerCreatedV2Event(customerId, "Neo", "Doe", "email@email.pl", "123", LocalDateTime.now(), "admin"),
			new CustomerEmailChangedEvent(customerId, "new@email.pl", LocalDateTime.now(), "admin2"),
			new CustomerEmailChangedEvent(customerId, "new2@email.pl", LocalDateTime.now(), "admin2"),
			new CustomerRenamedEvent(customerId, "John", LocalDateTime.now(), "admin2")
		);
		Mockito.when(eventStoreMock.loadEvents(customerId, Customer.class)).thenReturn(events);

		//when
		List<Event> upcastedEvents = upcastingEventStoreWrapper.loadEvents(customerId, Customer.class);

		//then
		Assertions.assertThat(upcastedEvents).hasSize(4);
		upcastedEvents.stream().forEach(
			event -> Assertions.assertThat(event.getClass()).isNotEqualTo(CustomerEmailChangedEvent.class)
		);
	}
}
