package pl.biltec.yaess.clp.adapters.store;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import org.junit.Before;
import org.junit.Test;

import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.customer.CustomerRepository;
import pl.biltec.yaess.clp.domain.customer.exception.ConcurrentModificationException;


public class CustomerEventStoreRepositoryTest {

	private CustomerRepository customerRepository;

	@Before
	public void setUp() throws Exception {

		customerRepository = new CustomerEventStoreRepository(new InMemoryCustomerEventStore());

	}

	@Test
	public void shouldSavedCustomerRemainTheSameAfterGetFromStore() throws Exception {

		//given
		Customer customer = new Customer("Jake");
		customer.rename("Johhhny");
		customer.rename("Bob");
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
		Customer customer = new Customer("Jake");
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
		Customer customer = new Customer("Jake");
		customerRepository.save(customer);
		Customer concurrentCustomer1 = customerRepository.get(customer.id());
		Customer concurrentCustomer2 = customerRepository.get(customer.id());

		//when
		concurrentCustomer1.rename("Jake2");
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