package pl.biltec.yaess.core.adapters.store.jpa;

import org.springframework.data.repository.CrudRepository;


/**
 * Spring Data
 */
public interface SnapshotRecordRepository extends CrudRepository<SnapshotRecord, Long> {

	SnapshotRecord findSnapshotRecordByRootAggregateIdEquals(String rootAggregateId);

	void deleteSnapshotRecordByRootAggregateIdEquals(String rootAggregateId);
}
