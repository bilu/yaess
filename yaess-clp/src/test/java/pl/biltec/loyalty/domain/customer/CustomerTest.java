package pl.biltec.loyalty.domain.customer;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.customer.CustomerId;
import pl.biltec.yaess.clp.domain.customer.event.CustomerCreatedEvent;
import pl.biltec.yaess.clp.domain.customer.event.CustomerEvent;
import pl.biltec.yaess.clp.domain.customer.event.CustomerRenamedEvent;


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
		CustomerEvent event1 = new CustomerCreatedEvent(new CustomerId(), "test newName 1", LocalDateTime.now());
		CustomerEvent event2 = new CustomerRenamedEvent(new CustomerId(), "test newName 2", LocalDateTime.now());
		List<CustomerEvent> customerEvents = Arrays.asList(event1, event2);

		//when
		Customer customer = new Customer(customerEvents);

		//then
		Assertions.assertThat(customer).isNotNull();
		Assertions.assertThat(customer.getUncommittedEvents()).hasSize(0);
	}
}