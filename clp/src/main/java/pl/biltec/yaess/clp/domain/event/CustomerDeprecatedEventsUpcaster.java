package pl.biltec.yaess.clp.domain.event;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import pl.biltec.yaess.core.adapters.store.DeprecatedEventsUpcaster;
import pl.biltec.yaess.core.domain.Event;


public class CustomerDeprecatedEventsUpcaster extends DeprecatedEventsUpcaster {

	public CustomerDeprecatedEventsUpcaster() {

		define(CustomerEmailChangedEvent.class, customerChangedEmailEventConverter());
		define(CustomerGenderChangedEvent.class, customerGenderChangedEventConverter());
		define(CustomerRenamedEvent.class, customerRenamedEventConverter());

	}

	private EventConverter<CustomerRenamedEvent> customerRenamedEventConverter() {

		return customerRenamedEvent -> {

			List<Event> upcasted = new LinkedList<>();

			String[] split = customerRenamedEvent.getNewName().trim().split(" ");
			if (split.length > 0) {
				upcasted
					.add(
						new CustomerFirstNameChangedEvent(
							customerRenamedEvent.rootAggregateId(),
							split[0],
							customerRenamedEvent.created(),
							customerRenamedEvent.originator())
					);
			}
			if (split.length > 1) {
				upcasted.add(
					new CustomerSurnameChangedEvent(
						customerRenamedEvent.rootAggregateId(),
						joinStringsSkippingFirstElement(split),
						customerRenamedEvent.created(),
						customerRenamedEvent.originator())
				);
			}
			return upcasted;
		};
	}

	private String joinStringsSkippingFirstElement(String[] split) {

		return Arrays.asList(split).stream().skip(1).collect(Collectors.joining(" "));
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
