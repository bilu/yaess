package pl.biltec.yaess.loyalty.domain.customer;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import org.junit.Test;

import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.event.CustomerCreatedEvent;
import pl.biltec.yaess.clp.event.CustomerRenamedEvent;
import pl.biltec.yaess.core.common.exception.ContractBrokenException;
import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerTest {


	@Test
	public void shouldCreateCustomer() throws Exception {
		//given
		Event event1 = new CustomerCreatedEvent(new RootAggregateId(), "test newName 1", LocalDateTime.now());
		Event event2 = new CustomerRenamedEvent(new RootAggregateId(), "test newName 2", LocalDateTime.now());

		//when
		Customer customer = new Customer("test newName 1");
		customer.rename("test newName 2");

		//then
		Assertions.assertThat(customer).isNotNull();
		Assertions.assertThat(customer.getUncommittedEvents()).hasSize(2);
	}


	@Test
	public void shouldRecreateCustomer() throws Exception {
		//given
		RootAggregateId RootAggregateId = new RootAggregateId();
		Event event1 = new CustomerCreatedEvent(RootAggregateId, "test newName 1", LocalDateTime.now());
		Event event2 = new CustomerRenamedEvent(RootAggregateId, "test newName 2", LocalDateTime.now());
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
		Event event1 = new CustomerCreatedEvent(RootAggregateId, "test newName 1", LocalDateTime.now());
		Event event2 = new CustomerRenamedEvent(RootAggregateId2, "test newName 2", LocalDateTime.now());
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