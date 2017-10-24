package pl.biltec.yaess.core.adapters.store.memory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import pl.biltec.yaess.core.common.Contract;
import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.Repository;
import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class InMemoryEventStoreRepository<ID extends RootAggregateId, EVENT extends Event, ROOT extends RootAggregate<ID, EVENT>> implements Repository<ID, EVENT, ROOT> {

	protected InMemoryEventStore eventStore;
	private Class<ROOT> rootClass;

	public InMemoryEventStoreRepository(InMemoryEventStore eventStore, Class<ROOT> rootClass) {

		Contract.notNull(eventStore, "eventStore");
		Contract.notNull(rootClass, "rootClass");

		this.eventStore = eventStore;
		// TODO [bilu] 22.10.17 switch direct Class usage to something more dynamic eg ParameterizedType
		this.rootClass = rootClass;
	}

	private ROOT invokeConstructor(Class<ROOT> clazz, List<EVENT> events) {

		try {
			Constructor<ROOT> constructor = clazz.getConstructor(List.class);
			return constructor.newInstance(events);
		}
		catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Problem with invocation of " + clazz.getSimpleName() + " constructor", e);
		}
	}

	@Override
	public ROOT get(ID id) {

		// TODO [bilu] 24.10.17 constructor vs newInstance, performance context 
		//		return new ROOT(eventStore.loadEvents(rootAggregateId));
		return invokeConstructor(rootClass, (List<EVENT>) eventStore.loadEvents(id.toString(), rootAggregateName()));
	}

	@Override
	public void save(ROOT rootAggregate) {

		eventStore.appendEvents(rootAggregate.id().toString(), rootAggregateName(), rootAggregate.getUncommittedEvents(), rootAggregate.concurrencyVersion());
		rootAggregate.clearUncommittedEvents();
	}

	private String rootAggregateName() {

		return rootClass.getSimpleName();
	}

	@Override
	public boolean exists(ID id) {

		return eventStore.exists(id.toString(), rootAggregateName());
	}
}
