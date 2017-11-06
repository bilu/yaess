package pl.biltec.yaess;

import java.util.UUID;

import org.junit.Test;

import pl.biltec.yaess.clp.adapters.store.CustomerRepositoryOverEventStore;
import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.ports.customer.CustomerCommandService;
import pl.biltec.yaess.clp.ports.customer.command.ChangeCustomerNameCommand;
import pl.biltec.yaess.clp.ports.customer.command.CreateCustomerCommand;
import pl.biltec.yaess.core.adapters.store.memory.InMemoryEventStore;
import pl.biltec.yaess.core.adapters.store.memory.InMemorySnapshotStore;
import pl.biltec.yaess.core.adapters.store.memory.InMemoryUniqueValuesStore;


public class CustomerCommandServiceBDDTest extends BDDTest<CustomerCommandServiceBDDTest, CustomerCommandService> {

	public CustomerCommandServiceBDDTest() {

		super(new CustomerCommandService(
			new CustomerRepositoryOverEventStore(
				new InMemoryEventStore(),
				new InMemorySnapshotStore(),
				new InMemoryUniqueValuesStore(),
				Customer.class
			),
			command -> true));
	}

	@Test
	public void shouldXX() throws Exception {
		//given
		String customerId = UUID.randomUUID().toString();
		given(
			new CreateCustomerCommand(customerId, "a", "b", "c", "d"),
			new ChangeCustomerNameCommand(customerId, "b", "c")
		);

		//when

		//then
//		Assertions.assertThat(result).isNotNull();
	}
}
