package pl.biltec.yaess.clp.domain.customer;

import java.util.UUID;

import pl.biltec.yaess.core.domain.AggregateId;


public class CustomerId extends AggregateId {

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
