package pl.biltec.yaess.clp.ports.customer;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import org.junit.Before;
import org.junit.Test;

import pl.biltec.yaess.clp.adapters.store.CustomerRepositoryOverEventStore;
import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.event.CustomerCreatedEvent;
import pl.biltec.yaess.core.adapters.store.EventStore;
import pl.biltec.yaess.core.adapters.store.SingleEventSubscriber;
import pl.biltec.yaess.core.adapters.store.SnapshotStore;
import pl.biltec.yaess.core.adapters.store.UniqueValuesStore;
import pl.biltec.yaess.core.adapters.store.memory.InMemoryEventStore;
import pl.biltec.yaess.core.adapters.store.memory.InMemorySnapshotStore;
import pl.biltec.yaess.core.adapters.store.memory.InMemoryUniqueValuesStore;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerCommandServiceTest {

	private CustomerCommandService customerCommandService;
	private CustomerRepositoryOverEventStore customerRepository;

	@Before
	public void setUp() throws Exception {

		EventStore eventStore = new InMemoryEventStore();
		SnapshotStore snapshotStore = new InMemorySnapshotStore();
		UniqueValuesStore uniqueValueStore = new InMemoryUniqueValuesStore();
		eventStore.addEventSubscriber(emailsUpater(uniqueValueStore));
		customerRepository = new CustomerRepositoryOverEventStore(eventStore, snapshotStore, uniqueValueStore, Customer.class);
		customerCommandService = new CustomerCommandService(customerRepository);

	}

	private SingleEventSubscriber<CustomerCreatedEvent> emailsUpater(UniqueValuesStore uniqueValueStore) {

		return new SingleEventSubscriber<CustomerCreatedEvent>(CustomerCreatedEvent.class) {

			@Override
			public void handle(CustomerCreatedEvent customerCreatedEvent) {
				uniqueValueStore.addUnique(Customer.class, customerCreatedEvent.rootAggregateId(), "EMAIL", customerCreatedEvent.getEmail());
			}
		};
	}

	@Test
	public void shouldCreateCustomer() throws Exception {
		//when
		String customerId = customerCommandService.createCustomer("Abra", "ham@email.pl");

		//then
		Assertions.assertThat(customerRepository.exists(new RootAggregateId(customerId))).isTrue();
	}

	@Test
	public void shouldCreateTwoCustomers() throws Exception {
		//when
		String customerId = customerCommandService.createCustomer("Abra", "ham@email.pl");
		String customerId2 = customerCommandService.createCustomer("Abra", "ham_2@email.pl");

		//then
		Assertions.assertThat(customerRepository.exists(new RootAggregateId(customerId))).isTrue();
		Assertions.assertThat(customerRepository.exists(new RootAggregateId(customerId2))).isTrue();
	}

	@Test
	public void shouldNotAllowToCreateCustomerWithTheSameEmail() throws Exception {
		//given
		String customerId = customerCommandService.createCustomer("Abra", "ham@email.pl");

		try {
			//when
			//awaits for async unique email loads
			Thread.sleep(500);
			customerCommandService.createCustomer("Abra", "ham@email.pl");
			Fail.fail("exception expected");
		}
		catch (Exception e) {
			//then
			Assertions.assertThat(e).hasMessageContaining("ham@email.pl");
		}
	}

}