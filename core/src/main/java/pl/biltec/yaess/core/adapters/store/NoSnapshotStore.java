package pl.biltec.yaess.core.adapters.store;

import java.util.Optional;

import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class NoSnapshotStore implements SnapshotStore {

	@Override
	public boolean exists(RootAggregateId id) {

		return false;
	}

	@Override
	public Optional<? extends RootAggregate> loadSnapshot(RootAggregateId id) {

		return Optional.empty();
	}

	@Override
	public void createAndSaveSnapshot(RootAggregate rootAggregate) {

	}
}
