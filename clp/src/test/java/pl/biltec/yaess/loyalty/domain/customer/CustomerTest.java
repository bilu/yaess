package pl.biltec.yaess.loyalty.domain.customer;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import org.junit.Test;

import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.customer.CustomerId;
import pl.biltec.yaess.clp.domain.customer.event.CustomerCreatedEvent;
import pl.biltec.yaess.clp.domain.customer.event.CustomerEvent;
import pl.biltec.yaess.clp.domain.customer.event.CustomerRenamedEvent;
import pl.biltec.yaess.core.common.exception.ContractBrokenException;


public class CustomerTest {


	@Test
	public void shouldCreateCustomer() throws Exception {
		//given
		CustomerEvent event1 = new CustomerCreatedEvent(new CustomerId(), "test newName 1", LocalDateTime.now());
		CustomerEvent event2 = new CustomerRenamedEvent(new CustomerId(), "test newName 2", LocalDateTime.now());

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
		CustomerId customerId = new CustomerId();
		CustomerEvent event1 = new CustomerCreatedEvent(customerId, "test newName 1", LocalDateTime.now());
		CustomerEvent event2 = new CustomerRenamedEvent(customerId, "test newName 2", LocalDateTime.now());
		List<CustomerEvent> customerEvents = Arrays.asList(event1, event2);

		//when
		Customer customer = new Customer(customerEvents);

		//then
		Assertions.assertThat(customer).isNotNull();
		Assertions.assertThat(customer.getUncommittedEvents()).hasSize(0);
	}

	@Test
	public void shouldBeImpossibleToRecreateCustomerWithIncorrectCustomerId() throws Exception {
		//given
		CustomerId customerId = new CustomerId();
		CustomerId customerId2 = new CustomerId();
		CustomerEvent event1 = new CustomerCreatedEvent(customerId, "test newName 1", LocalDateTime.now());
		CustomerEvent event2 = new CustomerRenamedEvent(customerId2, "test newName 2", LocalDateTime.now());
		List<CustomerEvent> customerEvents = Arrays.asList(event1, event2);

		//when
		try {
			new Customer(customerEvents);
			Fail.fail("Exception expected");
		}
		catch (Exception e) {
			//then
			Assertions.assertThat(e)
				.isInstanceOf(ContractBrokenException.class)
				.hasMessageContaining(customerId.toString())
				.hasMessageContaining(customerId2.toString());

		}

	}

}