package pl.biltec.yaess.clp.domain.customer.exception;

import pl.biltec.yaess.core.common.exception.DomainOperationException;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class CustomerAlreadyCreatedException extends DomainOperationException {

	public CustomerAlreadyCreatedException(RootAggregateId RootAggregateId) {

		super(RootAggregateId.toString());
	}
}
