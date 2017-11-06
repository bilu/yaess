package pl.biltec.yaess.clp.adapters;

import java.time.LocalDateTime;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.google.gson.Gson;

import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.event.CustomerRenamedEvent;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class GsonTest {

	@Test
	public void shouldGsonSerializeAndDeserializeCustomer() throws Exception {
		//given
		Customer customer = new Customer(UUID.randomUUID().toString(), "zenek", "test@email.pl", "77112233445", "admin");
		customer.clearUncommittedEvents();
		String json = new Gson().toJson(customer);

		//when
		Customer customerRestored = new Gson().fromJson(json, Customer.class);
		String jsonRestored = new Gson().toJson(customerRestored);

		//then
		Assertions.assertThat(customer).isEqualTo(customerRestored);
		Assertions.assertThat(json).isEqualTo(jsonRestored);
	}

	@Test
	public void shouldGsonSerializeAndDeserializeEvent() throws Exception {
		//given
		CustomerRenamedEvent event = new CustomerRenamedEvent(new RootAggregateId(), "krz", LocalDateTime.now(), "admin");
		String json = new Gson().toJson(event);


		//when
		CustomerRenamedEvent eventRestored = new Gson().fromJson(json, CustomerRenamedEvent.class);
		String jsonRestored = new Gson().toJson(eventRestored);

		//then
		Assertions.assertThat(event).isEqualTo(eventRestored);
		Assertions.assertThat(json).isEqualTo(jsonRestored);
	}

}
