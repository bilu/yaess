package pl.biltec.yaess.clp.adapters.store;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import org.junit.Before;
import org.junit.Test;

import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.core.adapters.store.RepositoryOverEventStore;
import pl.biltec.yaess.core.adapters.store.memory.InMemoryEventStore;
import pl.biltec.yaess.core.adapters.store.memory.InMemorySnapshotStore;
import pl.biltec.yaess.core.adapters.store.memory.InMemoryUniqueValuesStore;
import pl.biltec.yaess.core.common.exception.ConcurrentModificationException;
import pl.biltec.yaess.core.domain.Repository;


public class EventStoreRepositoryTest {

	private Repository<Customer> customerRepository;

	@Before
	public void setUp() throws Exception {

		customerRepository = new RepositoryOverEventStore(new InMemoryEventStore(), new InMemorySnapshotStore(), new InMemoryUniqueValuesStore(), Customer.class);

	}

	@Test
	public void shouldCreateAndRecreateFromSnapshot() throws Exception {
		//given
		Customer customer = new Customer(UUID.randomUUID().toString(), "Jake", "Blue", "test@email.pl", "77112233445", "admin");
		for (int i = 0; i < 103; i++) {
			customer.changeFirstName("handle #" + i, "admin");
			customerRepository.save(customer);
		}
		//when
		Customer recreatedCustomer = customerRepository.get(customer.id());
		//then
		Assertions.assertThat(recreatedCustomer).isEqualTo(customer);
	}

	@Test
	public void shouldSavedCustomerRemainTheSameAfterGetFromStore() throws Exception {

		//given
		Customer customer = new Customer(UUID.randomUUID().toString(), "Jake", "Blue", "test@email.pl", "77112233445", "admin");
		customer.changeFirstName("Johhhny", "admin");
		customer.changeFirstName("Bob", "admin");
		customerRepository.save(customer);

		//when
		Customer returnedFromRepository = customerRepository.get(customer.id());

		//then
		Assertions.assertThat(returnedFromRepository)
			.isNotNull()
			.isEqualTo(customer);
	}

	@Test
	public void shouldConcurrentSaveWhenNoModificationMadeBeAllowed() throws Exception {
		//given
		Customer customer = new Customer(UUID.randomUUID().toString(), "Jake", "Blue", "test@email.pl", "77112233445", "admin");
		customerRepository.save(customer);
		Customer concurrentCustomer1 = customerRepository.get(customer.id());
		Customer concurrentCustomer2 = customerRepository.get(customer.id());

		//when
		customerRepository.save(concurrentCustomer1);
		customerRepository.save(concurrentCustomer2);

		//then
		//No exception thrown
	}

	@Test
	public void shouldConcurrentSaveWhenModificationMadeThrowException() throws Exception {
		//given
		Customer customer = new Customer(UUID.randomUUID().toString(), "Jake", "Blue", "test@email.pl", "77112233445", "admin");
		customerRepository.save(customer);
		Customer concurrentCustomer1 = customerRepository.get(customer.id());
		Customer concurrentCustomer2 = customerRepository.get(customer.id());

		//when
		concurrentCustomer1.changeFirstName("Jake2", "admin");
		customerRepository.save(concurrentCustomer1);

		try {
			customerRepository.save(concurrentCustomer2);
			Fail.fail("Exception expected");
		}
		catch (Exception e) {
			Assertions.assertThat(e).isInstanceOf(ConcurrentModificationException.class);
		}

		//then
		//No exception thrown
	}

}