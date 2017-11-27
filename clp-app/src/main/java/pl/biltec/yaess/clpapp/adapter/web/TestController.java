package pl.biltec.yaess.clpapp.adapter.web;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.biltec.yaess.clp.adapters.store.CustomerRepositoryOverEventStore;
import pl.biltec.yaess.clp.domain.customer.Customer;
import pl.biltec.yaess.clp.domain.customer.CustomerRepository;
import pl.biltec.yaess.clp.domain.event.CustomerCreatedV3Event;
import pl.biltec.yaess.clp.domain.event.CustomerEmailChangedV2Event;
import pl.biltec.yaess.clp.ports.customer.CustomerCommandService;
import pl.biltec.yaess.clp.ports.customer.command.ChangeCustomerEmailCommand;
import pl.biltec.yaess.clp.ports.customer.command.CreateCustomerCommand;
import pl.biltec.yaess.core.adapters.store.EventStore;
import pl.biltec.yaess.core.adapters.store.SingleEventSubscriber;
import pl.biltec.yaess.core.adapters.store.SnapshotStore;
import pl.biltec.yaess.core.adapters.store.UniqueValuesStore;
import pl.biltec.yaess.core.domain.RootAggregateId;


@RestController
class TestController {

	//	@Autowired
	//	private EventStore eventStore;
	//	private EventRecordRepository eventRecordRepository;
	private CustomerCommandService customerCommandService;
	private CustomerRepository customerRepository;
	private Map<String, Object> result = new HashMap<>();

	@Autowired
	public TestController(
		EventStore eventStore,
		SnapshotStore snapshotStore,
		UniqueValuesStore uniqueValuesStore) {

		eventStore.addEventSubscriber(emailsCreatedUpdater(uniqueValuesStore));
		eventStore.addEventSubscriber(emailsChangedUpdater(uniqueValuesStore));

		customerRepository = new CustomerRepositoryOverEventStore(eventStore, snapshotStore, uniqueValuesStore, Customer.class);
		customerCommandService = new CustomerCommandService(customerRepository, (command -> true));

	}

	private SingleEventSubscriber<CustomerCreatedV3Event> emailsCreatedUpdater(UniqueValuesStore uniqueValueStore) {

		return new SingleEventSubscriber<CustomerCreatedV3Event>(CustomerCreatedV3Event.class) {

			@Override
			public void handle(CustomerCreatedV3Event customerCreatedV3Event) {

				uniqueValueStore.addUnique(Customer.class, customerCreatedV3Event.rootAggregateId(), "EMAIL", customerCreatedV3Event.getEmail());
			}
		};
	}

	private SingleEventSubscriber<CustomerEmailChangedV2Event> emailsChangedUpdater(UniqueValuesStore uniqueValueStore) {

		return new SingleEventSubscriber<CustomerEmailChangedV2Event>(CustomerEmailChangedV2Event.class) {

			@Override
			public void handle(CustomerEmailChangedV2Event customerEmailChangedV2Event) {

				uniqueValueStore.addUnique(Customer.class, customerEmailChangedV2Event.rootAggregateId(), "EMAIL", customerEmailChangedV2Event.getNewEmail());
			}
		};
	}

	@GetMapping("/test")
	Map<String, Object> test(
		@RequestParam(required = false, defaultValue = "") String id) {

		result.put("timestamp", LocalDateTime.now());

		int randomInt = RandomUtils.nextInt(0, 100);
		if (StringUtils.hasText(id)) {
			customerCommandService.handle(new ChangeCustomerEmailCommand(id, "admin2", "zmieniony-Pablo" + randomInt));
			result.put(id, customerRepository.get(new RootAggregateId(id)).toString());
		}
		else {
			int randomInt2 = RandomUtils.nextInt(0, 10);
			String customerId = UUID.randomUUID().toString();
			customerCommandService
				.handle(new CreateCustomerCommand(customerId, "admin", "Pablo" + randomInt2, "Picasso" + randomInt2, "pablo" + randomInt2 + "@picasso.pl", "12121234563"));
			result.put(customerId, customerRepository.get(new RootAggregateId(customerId)).toString());
		}

		return result;
	}
}
