package pl.biltec.yaess.clp.domain.customer;

import java.util.UUID;

import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerId extends RootAggregateId {

	public CustomerId() {

		super();
	}

	public CustomerId(UUID id) {

		super(id);
	}

	public CustomerId(String id) {

		super(id);
	}

}
