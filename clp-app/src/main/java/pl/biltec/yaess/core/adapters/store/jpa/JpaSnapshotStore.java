
package pl.biltec.yaess.core.adapters.store.jpa;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import pl.biltec.yaess.core.adapters.store.SnapshotStore;
import pl.biltec.yaess.core.common.Contract;
import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


@Service
public class JpaSnapshotStore implements SnapshotStore {

	private static final Logger logger = LoggerFactory.getLogger(JpaSnapshotStore.class);
	private Gson gson = new Gson();
	private SnapshotRecordRepository snapshotRecordRepository;

	@Autowired
	public JpaSnapshotStore(SnapshotRecordRepository snapshotRecordRepository) {

		this.snapshotRecordRepository = snapshotRecordRepository;
	}

	@Override
	public Optional<? extends RootAggregate> loadSnapshot(RootAggregateId id) {

		Contract.notNull(id, "id");
		String key = id.toString();
		SnapshotRecord snapshotRecord = snapshotRecordRepository.findSnapshotRecordByRootAggregateIdEquals(id.toString());
		return Optional.ofNullable(snapshotRecord)
			.map(snapshot -> {
				try {
					RootAggregate rootAggregate = (RootAggregate) gson.fromJson(snapshot.getRootAggregateAsJson(), Class.forName(snapshotRecord.getRootAggregateClassName()));
					logger.info("Snapshot for {} recreated as object {}", key, rootAggregate);
					return rootAggregate;
				}
				catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			});
	}

	@Override
	public void createAndSaveSnapshot(RootAggregate rootAggregate) {

		Contract.notNull(rootAggregate, "rootAggregate");
		String rootAggregateId = rootAggregate.id().toString();
		String rootAggregateAsJson = gson.toJson(rootAggregate);
		snapshotRecordRepository.deleteSnapshotRecordByRootAggregateIdEquals(rootAggregateId);
		snapshotRecordRepository.save(new SnapshotRecord(rootAggregateId, rootAggregate.getClass().getName(), rootAggregateAsJson));
		logger.info("Snapshot for {} created as json {}", rootAggregateId, rootAggregateAsJson);
	}

}
