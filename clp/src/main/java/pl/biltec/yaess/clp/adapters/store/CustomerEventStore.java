package pl.biltec.yaess.clp.adapters.store;

import pl.biltec.yaess.core.adapters.store.EventStore;
import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public interface CustomerEventStore extends EventStore<RootAggregateId, Event> {

}
