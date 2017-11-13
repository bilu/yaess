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
		define(CustomerCreatedV2Event.class, customerCreatedEventV2Converter());
		define(CustomerEmailChangedEvent.class, customerChangedEmailEventConverter());
		define(CustomerGenderChangedEvent.class, customerGenderChangedEventConverter());
		define(CustomerRenamedEvent.class, customerRenamedEventConverter());
		define(CustomerSurnameChangedEvent.class, customerSurnameChangedEventConverter());

	}

	private EventConverter<CustomerSurnameChangedEvent> customerSurnameChangedEventConverter() {

		return deprecatedEvent ->
			Arrays.asList(
				new CustomerLastNameChangedEvent(
					deprecatedEvent.rootAggregateId(),
					deprecatedEvent.getSurname(),
					deprecatedEvent.created(),
					deprecatedEvent.originator())
			);
	}

	private EventConverter<CustomerCreatedV2Event> customerCreatedEventV2Converter() {

		return deprecatedEvent ->
			Arrays.asList(
				new CustomerCreatedV3Event(
					deprecatedEvent.rootAggregateId(),
					deprecatedEvent.getFirstName(),
					deprecatedEvent.getSurname(),
					deprecatedEvent.getEmail(),
					deprecatedEvent.getPersonalIdNumber(),
					deprecatedEvent.created(),
					deprecatedEvent.originator())
			);

	}

	private EventConverter<CustomerCreatedEvent> customerCreatedEventConverter() {

		return deprecatedEvent -> {
			List<String> splittedName = splitNameBySpace(deprecatedEvent.getName());
			String firstName = splittedName.get(0);
			String surname = splittedName.stream().skip(1).collect(Collectors.joining(" "));
			return Arrays.asList(
				new CustomerCreatedV2Event(
					deprecatedEvent.rootAggregateId(),
					firstName,
					surname,
					deprecatedEvent.getEmail(),
					deprecatedEvent.getPersonalIdNumber(),
					deprecatedEvent.created(),
					deprecatedEvent.originator())
			);
		};
	}

	private static List<String> splitNameBySpace(String name) {

		Contract.notNull(name, "name");
		return Arrays.asList(name.trim().split(" "));
	}

	private EventConverter<CustomerRenamedEvent> customerRenamedEventConverter() {

		return deprecatedEvent -> {

			List<Event> upcasted = new LinkedList<>();

			List<String> splitted = splitNameBySpace(deprecatedEvent.getNewName());
			if (splitted.size() > 0) {
				upcasted
					.add(
						new CustomerFirstNameChangedEvent(
							deprecatedEvent.rootAggregateId(),
							splitted.get(0),
							deprecatedEvent.created(),
							deprecatedEvent.originator())
					);
			}
			if (splitted.size() > 1) {
				upcasted.add(
					new CustomerSurnameChangedEvent(
						deprecatedEvent.rootAggregateId(),
						splitted.stream().skip(1).collect(Collectors.joining(" ")),
						deprecatedEvent.created(),
						deprecatedEvent.originator())
				);
			}
			return upcasted;
		};
	}

	private EventConverter<CustomerGenderChangedEvent> customerGenderChangedEventConverter() {

		return deprecatedEvent -> Collections.emptyList();
	}

	private EventConverter<CustomerEmailChangedEvent> customerChangedEmailEventConverter() {

		return deprecatedEvent -> Arrays.asList(
			new CustomerEmailChangedV2Event(
				deprecatedEvent.rootAggregateId(),
				"unknown",
				deprecatedEvent.getEmail(),
				deprecatedEvent.created(),
				deprecatedEvent.originator()));
	}

}
