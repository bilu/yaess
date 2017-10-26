package pl.biltec.yaess.core.adapters.store;

import java.util.Optional;

import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


public interface SnapshotStore {

//	// TODO [bilu] 25.10.17 is it really necessary?
//	boolean exists(RootAggregateId id);

	Optional<? extends RootAggregate> loadSnapshot(RootAggregateId id);

	void createAndSaveSnapshot(RootAggregate rootAggregate);

}

