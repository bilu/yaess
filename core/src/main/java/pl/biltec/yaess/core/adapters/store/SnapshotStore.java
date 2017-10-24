package pl.biltec.yaess.core.adapters.store;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


public interface SnapshotStore<ID extends RootAggregateId, EVENT extends Event, ROOT extends RootAggregate<ID, EVENT>> {

	boolean exists(ID id);

	ROOT loadSnapshot(ID id);

	void saveSnapshot(ROOT rootAggregate);
}

