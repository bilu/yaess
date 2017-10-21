package pl.biltec.yaess.clp.adapters.store;

import pl.biltec.yaess.clp.domain.customer.CustomerId;
import pl.biltec.yaess.clp.domain.customer.event.CustomerEvent;
import pl.biltec.yaess.core.adapters.store.EventStore;


public interface CustomerEventStore extends EventStore<CustomerId, CustomerEvent> {

}
