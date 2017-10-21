package pl.biltec.yaess.clp.domain.customer;

import pl.biltec.yaess.clp.domain.customer.event.CustomerEvent;
import pl.biltec.yaess.core.domain.Repository;


public interface CustomerRepository extends Repository<CustomerId, CustomerEvent, Customer> {

}
