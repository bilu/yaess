package pl.biltec.yaess.core.adapters.store;

import java.util.Optional;

import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


public interface SnapshotStore<ROOT extends RootAggregate> {

	boolean exists(RootAggregateId id);

	Optional<ROOT> loadSnapshot(RootAggregateId id);

	void saveSnapshot(ROOT rootAggregate);
}

