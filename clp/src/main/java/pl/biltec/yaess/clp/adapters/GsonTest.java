package pl.biltec.yaess.clp.adapters;

import java.time.LocalDateTime;

import com.google.gson.Gson;

import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.event.CustomerRenamedEvent;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class GsonTest {

	public static void main(String[] args) {


		Customer customer = new Customer("zenek");

//		customer.clearUncommittedEvents();
		String json = new Gson().toJson(customer);
		System.out.println(json);

		Customer customerRestored = new Gson().fromJson(json, Customer.class);

		System.out.println("customer.equals(customerRestored)="+customer.equals(customerRestored));

		String json2 = new Gson().toJson(customer);
		System.out.println(json2);



		CustomerRenamedEvent event = new CustomerRenamedEvent(new RootAggregateId(), "krz", LocalDateTime.now());

		json = new Gson().toJson(event);
		System.out.println(json);

		CustomerRenamedEvent eventRestored = new Gson().fromJson(json, CustomerRenamedEvent.class);

		System.out.println("event.equals(eventRestored)="+event.equals(eventRestored));

		json2 = new Gson().toJson(event);
		System.out.println(json2);

	}

}
