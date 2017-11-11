package pl.biltec.yaess.clp.domain.event;

import pl.biltec.yaess.core.adapters.store.DeprecatedEventsUpcaster;


public class CustomerDeprecatedEventsUpcaster extends DeprecatedEventsUpcaster {

	public CustomerDeprecatedEventsUpcaster() {

		define(CustomerChangedEmailEvent.class,
			customerChangedEmailEvent -> new CustomerChangedEmailV2Event(customerChangedEmailEvent.rootAggregateId(),
			"unknown",
			customerChangedEmailEvent.getEmail(),
			customerChangedEmailEvent.created(),
			customerChangedEmailEvent.originator()));

	}

}
