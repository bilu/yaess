package pl.biltec.yaess.clp.ports.customer;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import org.junit.Before;
import org.junit.Test;

import pl.biltec.yaess.clp.adapters.store.CustomerRepositoryOverEventStore;
import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.event.CustomerChangedEmailEvent;
import pl.biltec.yaess.clp.domain.event.CustomerCreatedEvent;
import pl.biltec.yaess.clp.ports.customer.command.ChangeCustomerEmailCommand;
import pl.biltec.yaess.clp.ports.customer.command.CreateCustomerCommand;
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
		eventStore.addEventSubscriber(emailsCreatedUpdater(uniqueValueStore));
		eventStore.addEventSubscriber(emailsChangedUpdater(uniqueValueStore));
		customerRepository = new CustomerRepositoryOverEventStore(eventStore, snapshotStore, uniqueValueStore, Customer.class);
		AuthorizationService allowEveryoneAuthorizationService = command -> true;
		customerCommandService = new CustomerCommandService(customerRepository, allowEveryoneAuthorizationService);

	}

	private SingleEventSubscriber<CustomerCreatedEvent> emailsCreatedUpdater(UniqueValuesStore uniqueValueStore) {

		return new SingleEventSubscriber<CustomerCreatedEvent>(CustomerCreatedEvent.class) {

			@Override
			public void handle(CustomerCreatedEvent customerCreatedEvent) {

				uniqueValueStore.addUnique(Customer.class, customerCreatedEvent.rootAggregateId(), "EMAIL", customerCreatedEvent.getEmail());
			}
		};
	}

	private SingleEventSubscriber<CustomerChangedEmailEvent> emailsChangedUpdater(UniqueValuesStore uniqueValueStore) {

		return new SingleEventSubscriber<CustomerChangedEmailEvent>(CustomerChangedEmailEvent.class) {

			@Override
			public void handle(CustomerChangedEmailEvent customerChangedEmailEvent) {

				uniqueValueStore.addUnique(Customer.class, customerChangedEmailEvent.rootAggregateId(), "EMAIL", customerChangedEmailEvent.getEmail());
			}
		};
	}

	@Test
	public void shouldCreateCustomer() throws Exception {
		//when
		String customerId = UUID.randomUUID().toString();
		customerCommandService.handle(new CreateCustomerCommand(customerId, "admin", "Abra", "ham@email.pl", "77112233445"));

		//then
		Assertions.assertThat(customerRepository.exists(new RootAggregateId(customerId))).isTrue();
	}

	@Test
	public void shouldCreateTwoCustomers() throws Exception {
		//when
		String customerId = UUID.randomUUID().toString();
		String customerId2 = UUID.randomUUID().toString();
		customerCommandService.handle(new CreateCustomerCommand(customerId, "admin", "Abra", "ham@email.pl", "77112233445"));
		customerCommandService.handle(new CreateCustomerCommand(customerId2, "admin", "Abra", "ham_2@email.pl", "77112233445"));

		//then
		Assertions.assertThat(customerRepository.exists(new RootAggregateId(customerId))).isTrue();
		Assertions.assertThat(customerRepository.exists(new RootAggregateId(customerId2))).isTrue();
	}

	@Test
	public void shouldNotAllowToCreateCustomerWithTheSameEmail() throws Exception {
		//given
		String customerId = UUID.randomUUID().toString();
		String customerId2 = UUID.randomUUID().toString();
		customerCommandService.handle(new CreateCustomerCommand(customerId, "admin", "Abra", "ham@email.pl", "77112233445"));

		try {
			//when
			//awaits for async unique email loads
			waitForAsyncUpdateFinish();
			customerCommandService.handle(new CreateCustomerCommand(customerId2, "admin", "Abra", "ham@email.pl", "77112233445"));
			Fail.fail("exception expected");
		}
		catch (Exception e) {
			//then
			Assertions.assertThat(e).hasMessageContaining("ham@email.pl");
		}
	}

	@Test
	public void shouldNotAllowToChangeEmailToExistingOne() throws Exception {
		//given
		String customerId = UUID.randomUUID().toString();
		String customerId2 = UUID.randomUUID().toString();
		String customerId3 = UUID.randomUUID().toString();
		customerCommandService.handle(new CreateCustomerCommand(customerId, "admin", "Abra", "ham@email.pl", "77112233445"));
		customerCommandService.handle(new CreateCustomerCommand(customerId2, "admin", "Abra", "ham_2@email.pl", "77112233445"));
		customerCommandService.handle(new CreateCustomerCommand(customerId3, "admin", "Abra", "ham_3@email.pl", "77112233445"));
		Assertions.assertThat(customerRepository.exists(new RootAggregateId(customerId))).isTrue();
		Assertions.assertThat(customerRepository.exists(new RootAggregateId(customerId2))).isTrue();
		Assertions.assertThat(customerRepository.exists(new RootAggregateId(customerId3))).isTrue();

		try {
			//when
			//awaits for async unique email loads
			waitForAsyncUpdateFinish();
			customerCommandService.handle(new ChangeCustomerEmailCommand(customerId3, "admin", "ham@email.pl"));
			Fail.fail("exception expected");
		}
		catch (Exception e) {
			//then
			Assertions.assertThat(e).hasMessageContaining("ham@email.pl");
		}
	}

	@Test
	public void shouldNotAllowToChangeEmailToExistingOneWithMultipleEmailChange() throws Exception {
		//given
		String customerId = UUID.randomUUID().toString();
		String customerId2 = UUID.randomUUID().toString();
		customerCommandService.handle(new CreateCustomerCommand(customerId, "admin", "Abra", "ham@email.pl", "77112233445"));
		customerCommandService.handle(new CreateCustomerCommand(customerId2, "admin", "Abra", "ham_2@email.pl", "77112233445"));
		Assertions.assertThat(customerRepository.exists(new RootAggregateId(customerId))).isTrue();
		Assertions.assertThat(customerRepository.exists(new RootAggregateId(customerId2))).isTrue();
		customerCommandService.handle(new ChangeCustomerEmailCommand(customerId2, "admin", "ham_3@email.pl"));

		try {
			//when
			//awaits for async unique email loads
			waitForAsyncUpdateFinish();
			customerCommandService.handle(new ChangeCustomerEmailCommand(customerId, "admin", "ham_3@email.pl"));
			Fail.fail("exception expected");
		}
		catch (Exception e) {
			//then
			Assertions.assertThat(e).hasMessageContaining("ham_3@email.pl");
		}
	}

	@Test
	public void shouldAllowToChangeEmailToNotExistingOne() throws Exception {
		//given
		String customerId = UUID.randomUUID().toString();
		String customerId2 = UUID.randomUUID().toString();
		String customerId3 = UUID.randomUUID().toString();
		customerCommandService.handle(new CreateCustomerCommand(customerId, "admin", "Abra", "ham@email.pl", "77112233445"));
		customerCommandService.handle(new CreateCustomerCommand(customerId2, "admin", "Abra", "ham_2@email.pl", "77112233445"));
		customerCommandService.handle(new CreateCustomerCommand(customerId3, "admin", "Abra", "ham_3@email.pl", "77112233445"));
		Assertions.assertThat(customerRepository.exists(new RootAggregateId(customerId))).isTrue();
		Assertions.assertThat(customerRepository.exists(new RootAggregateId(customerId2))).isTrue();
		Assertions.assertThat(customerRepository.exists(new RootAggregateId(customerId3))).isTrue();

		//when
		waitForAsyncUpdateFinish();
		customerCommandService.handle(new ChangeCustomerEmailCommand(customerId3, "admin", "ham_4@email.pl"));

		//then
		//		no exceptin thrown
	}

	private void waitForAsyncUpdateFinish() throws InterruptedException {

		Thread.sleep(200);
	}

}