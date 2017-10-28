package pl.biltec.yaess.core.adapters.store;

import java.util.Optional;

import pl.biltec.yaess.core.common.annotation.NN;
import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


public interface SnapshotStore {

	Optional<? extends RootAggregate> loadSnapshot(RootAggregateId id);

	void createAndSaveSnapshot(RootAggregate rootAggregate);

}

