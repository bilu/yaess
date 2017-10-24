package pl.biltec.yaess.clp.domain.customer.exception;

import pl.biltec.yaess.core.common.exception.DomainOperationException;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerNotExistsException extends DomainOperationException {

	public CustomerNotExistsException(RootAggregateId RootAggregateId) {

		super(RootAggregateId.toString());
	}
}
