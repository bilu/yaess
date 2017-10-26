
package pl.biltec.yaess.core.adapters.store.memory;

import java.util.HashMap;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import pl.biltec.yaess.core.adapters.store.SnapshotStore;
import pl.biltec.yaess.core.common.Contract;
import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class InMemorySnapshotStore implements SnapshotStore {

	private static final Logger logger = LoggerFactory.getLogger(InMemorySnapshotStore.class);
	private Gson gson = new Gson();
	//<rootAggreagateId, json>
	private HashMap<String, SnapshotRecord> keyValueStore = new HashMap<>();

	@Override
	public Optional<? extends RootAggregate> loadSnapshot(RootAggregateId id) {

		Contract.notNull(id, "id");
		String key = id.toString();
		return Optional.ofNullable(keyValueStore.get(key))
			.map(snapshotRecord -> {
				try {
					RootAggregate rootAggregate = (RootAggregate) gson.fromJson(snapshotRecord.getRootAggregateAsJson(), Class.forName(snapshotRecord.getRootAggregateClassName()));
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
		String key = rootAggregate.id().toString();
		String rootAggregateAsJson = gson.toJson(rootAggregate);
		keyValueStore.put(key, new SnapshotRecord(rootAggregate.getClass().getName(), rootAggregateAsJson));
		logger.info("Snapshot for {} created as json {}", key, rootAggregateAsJson);
	}

}
