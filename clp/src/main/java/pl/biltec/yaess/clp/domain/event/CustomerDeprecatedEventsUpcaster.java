package pl.biltec.yaess.clp.domain.event;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import pl.biltec.yaess.core.adapters.store.DeprecatedEventsUpcaster;
import pl.biltec.yaess.core.common.Contract;
import pl.biltec.yaess.core.domain.Event;


public class CustomerDeprecatedEventsUpcaster extends DeprecatedEventsUpcaster {

	public CustomerDeprecatedEventsUpcaster() {

		define(CustomerCreatedEvent.class, customerCreatedEventConverter());
		define(CustomerEmailChangedEvent.class, customerChangedEmailEventConverter());
		define(CustomerGenderChangedEvent.class, customerGenderChangedEventConverter());
		define(CustomerRenamedEvent.class, customerRenamedEventConverter());

	}

	private EventConverter<CustomerCreatedEvent> customerCreatedEventConverter() {

		return customerCreatedEvent -> {
			List<String> splittedName = splitNameBySpace(customerCreatedEvent.getName());
			String firstName = splittedName.get(0);
			String surname = splittedName.stream().skip(1).collect(Collectors.joining(" "));
			return Arrays.asList(
				new CustomerCreatedV2Event(
					customerCreatedEvent.rootAggregateId(),
					firstName,
					surname,
					customerCreatedEvent.getEmail(),
					customerCreatedEvent.getPersonalIdNumber(),
					customerCreatedEvent.created(),
					customerCreatedEvent.originator())
			);
		};
	}

	private static List<String> splitNameBySpace(String name) {

		Contract.notNull(name, "name");
		return Arrays.asList(name.trim().split(" "));
	}

	private EventConverter<CustomerRenamedEvent> customerRenamedEventConverter() {

		return customerRenamedEvent -> {

			List<Event> upcasted = new LinkedList<>();

			List<String> splitted = splitNameBySpace(customerRenamedEvent.getNewName());
			if (splitted.size() > 0) {
				upcasted
					.add(
						new CustomerFirstNameChangedEvent(
							customerRenamedEvent.rootAggregateId(),
							splitted.get(0),
							customerRenamedEvent.created(),
							customerRenamedEvent.originator())
					);
			}
			if (splitted.size() > 1) {
				upcasted.add(
					new CustomerSurnameChangedEvent(
						customerRenamedEvent.rootAggregateId(),
						splitted.stream().skip(1).collect(Collectors.joining(" ")),
						customerRenamedEvent.created(),
						customerRenamedEvent.originator())
				);
			}
			return upcasted;
		};
	}

	private EventConverter<CustomerGenderChangedEvent> customerGenderChangedEventConverter() {

		return customerRenamedEvent -> Collections.emptyList();
	}

	private EventConverter<CustomerEmailChangedEvent> customerChangedEmailEventConverter() {

		return customerEmailChangedEvent -> Arrays.asList(
			new CustomerEmailChangedV2Event(
				customerEmailChangedEvent.rootAggregateId(),
				"unknown",
				customerEmailChangedEvent.getEmail(),
				customerEmailChangedEvent.created(),
				customerEmailChangedEvent.originator()));
	}

}
