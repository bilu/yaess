package pl.biltec.yaess.loyalty.domain.customer;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import org.junit.Test;

import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.event.CustomerCreatedV3Event;
import pl.biltec.yaess.clp.domain.event.CustomerFirstNameChangedEvent;
import pl.biltec.yaess.clp.domain.event.CustomerRenamedEvent;
import pl.biltec.yaess.core.common.exception.ContractBrokenException;
import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerTest {


	@Test
	public void shouldCreateCustomer() throws Exception {
		//when
		Customer customer = new Customer(UUID.randomUUID().toString(), "test firstName 1", "test surname 1", "test@email.pl", "77112233445", "admin");
		customer.changeFirstName("test firstName 2", "admin");

		//then
		Assertions.assertThat(customer).isNotNull();
		Assertions.assertThat(customer.getUncommittedEvents()).hasSize(2);
	}


	@Test
	public void shouldRecreateCustomer() throws Exception {
		//given
		RootAggregateId RootAggregateId = new RootAggregateId();
		Event event1 = new CustomerCreatedV3Event(RootAggregateId, "test newName 1", "test surname 1", "test@email.pl", "77112233445", LocalDateTime.now(), "admin");
		Event event2 = new CustomerFirstNameChangedEvent(RootAggregateId, "test newName 2", LocalDateTime.now(), "admin");
		List<Event> customerEvents = Arrays.asList(event1, event2);

		//when
		Customer customer = new Customer(customerEvents);

		//then
		Assertions.assertThat(customer).isNotNull();
		Assertions.assertThat(customer.getUncommittedEvents()).hasSize(0);
	}

	@Test
	public void shouldBeImpossibleToRecreateCustomerWithIncorrectRootAggregateId() throws Exception {
		//given
		RootAggregateId RootAggregateId = new RootAggregateId();
		RootAggregateId RootAggregateId2 = new RootAggregateId();
		Event event1 = new CustomerCreatedV3Event(RootAggregateId, "test newName 1", "test surname 1", "test@email.pl", "77112233445", LocalDateTime.now(), "admin");
		Event event2 = new CustomerRenamedEvent(RootAggregateId2, "test newName 2", LocalDateTime.now(), "admin");
		List<Event> customerEvents = Arrays.asList(event1, event2);

		//when
		try {
			new Customer(customerEvents);
			Fail.fail("Exception expected");
		}
		catch (Exception e) {
			//then
			Assertions.assertThat(e)
				.isInstanceOf(ContractBrokenException.class)
				.hasMessageContaining(RootAggregateId.toString())
				.hasMessageContaining(RootAggregateId2.toString());

		}

	}

}