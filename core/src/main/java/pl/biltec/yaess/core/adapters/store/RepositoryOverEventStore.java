package pl.biltec.yaess.core.adapters.store;

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
	private Class<ROOT> rootClass;

	public RepositoryOverEventStore(EventStore eventStore, Class<ROOT> rootClass) {

		this(eventStore, new NoSnapshotStore(), rootClass);
	}

	public RepositoryOverEventStore(EventStore eventStore, SnapshotStore snapshotStore, Class<ROOT> rootClass) {

		Contract.notNull(eventStore, "eventStore");
		Contract.notNull(snapshotStore, "snapshotStore");
		Contract.notNull(rootClass, "rootClass");

		this.eventStore = eventStore;
		this.snapshotStore = snapshotStore;
		this.rootClass = rootClass;
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

		return snapshotStore
			.loadSnapshot(id)
			.map(rootAggregate -> {
				List<Event> events = eventStore.loadEvents(id, rootClass, rootAggregate.concurrencyVersion(), Integer.MAX_VALUE);
				rootAggregate.apply(events);
				return (ROOT) rootAggregate;
			})
			// TODO [bilu] 24.10.17 constructor vs newInstance, performance context
			.orElse(invokeConstructor(rootClass, eventStore.loadEvents(id, rootClass)));
	}

	@Override
	public void save(ROOT rootAggregate) {
		eventStore.appendEvents(rootAggregate.id(), rootClass, rootAggregate.getUncommittedEvents(), rootAggregate.concurrencyVersion());
		rootAggregate.clearUncommittedEvents();

		// TODO [bilu] 25.10.17 repository (not SnapshotStore should manage where snapshot should be taken)
		// make it async
		if (rootAggregate.concurrencyVersion() % 50 == 0) {
			snapshotStore.createAndSaveSnapshot(rootAggregate);
		}
	}

	@Override
	public boolean exists(RootAggregateId id) {

		return eventStore.exists(id, rootClass);
	}
}
