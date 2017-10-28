package pl.biltec.yaess.clp.domain.customer;

import pl.biltec.yaess.core.domain.Repository;
import pl.biltec.yaess.core.domain.RootAggregateId;


public interface CustomerRepository extends Repository<Customer> {

	boolean isEmailUnique(String email);

	boolean isEmailUnique(RootAggregateId rootAggregateId, String email);

}
