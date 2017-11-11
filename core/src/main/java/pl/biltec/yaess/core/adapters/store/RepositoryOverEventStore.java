package pl.biltec.yaess.core.adapters.store;

import static pl.biltec.yaess.core.common.Contract.notNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import pl.biltec.yaess.core.common.Contract;
import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.Repository;
import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class RepositoryOverEventStore<ROOT extends RootAggregate> implements Repository<ROOT> {

	protected EventStore eventStore;
	protected SnapshotStore snapshotStore;
	protected UniqueValuesStore uniqueValuesStore;
	private Class<ROOT> rootClass;
	private int snapshotInterval = 5;

	public RepositoryOverEventStore(EventStore eventStore, Class<ROOT> rootClass) {

		this(eventStore, new NoSnapshotStore(), new NoUniqueValuesStore(), rootClass);
	}

	public RepositoryOverEventStore(EventStore eventStore, SnapshotStore snapshotStore, UniqueValuesStore uniqueValuesStore, Class<ROOT> rootClass) {

		this.eventStore = notNull(eventStore, "eventStore");
		this.snapshotStore = notNull(snapshotStore, "snapshotStore");
		this.rootClass = notNull(rootClass, "rootClass");
		this.uniqueValuesStore = notNull(uniqueValuesStore, "uniqueValuesStore");

	}

	private ROOT invokeConstructor(Class<ROOT> clazz, List<Event> events) {

		try {
			Constructor<ROOT> constructor = clazz.getConstructor(List.class);
			return constructor.newInstance(events);
		}
		catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Problem with invocation of " + clazz.getSimpleName() + " constructor", e);
		}
	}

	@Override
	public ROOT get(RootAggregateId id) {

		Contract.isTrue(exists(id), "Not found ID=" + id);

		return snapshotStore
			.loadSnapshot(id)
			.map(rootAggregate -> {
				List<Event> events = eventStore.loadEvents(id, rootClass, rootAggregate.concurrencyVersion(), Integer.MAX_VALUE);
				rootAggregate.apply(events);
				return (ROOT) rootAggregate;
			})
			// TODO [bilu] 24.10.17 constructor vs newInstance, performance context
			.orElseGet((() -> invokeConstructor(rootClass, eventStore.loadEvents(id, rootClass))));
	}

	@Override
	public void save(ROOT rootAggregate) {

		Contract.notNull(rootAggregate, "rootAggregate");
		eventStore.appendEvents(rootAggregate.id(), rootClass, rootAggregate.getUncommittedEvents(), rootAggregate.concurrencyVersion());
		rootAggregate.clearUncommittedEvents();

		// TODO [bilu] 25.10.17 repository (not SnapshotStore should manage where snapshot should be taken)
		// make it async
		if (rootAggregate.concurrencyVersion() % snapshotInterval == 0) {
			snapshotStore.createAndSaveSnapshot(rootAggregate);
		}
	}

	@Override
	public boolean exists(RootAggregateId id) {

		Contract.notNull(id, "id");
		return eventStore.exists(id, rootClass);
	}

	/**
	 * To be used by implementation
	 */
	protected boolean isUnique(RootAggregateId rootAggregateId, String attributeName, String attributeValue) {

		return uniqueValuesStore.isUnique(rootClass, rootAggregateId, attributeName, attributeValue);
	}

	/**
	 * To be used by implementation
	 */
	protected boolean isUnique(String attributeName, String attributeValue) {

		return uniqueValuesStore.isUnique(rootClass, attributeName, attributeValue);
	}

}
