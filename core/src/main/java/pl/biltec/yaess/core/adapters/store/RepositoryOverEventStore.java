//package pl.biltec.yaess.core.adapters.store;
//
//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;
//import java.util.List;
//
//import pl.biltec.yaess.core.common.Contract;
//import pl.biltec.yaess.core.domain.Event;
//import pl.biltec.yaess.core.domain.Repository;
//import pl.biltec.yaess.core.domain.RootAggregate;
//import pl.biltec.yaess.core.domain.RootAggregateId;
//
//
///**
// * Repository more like DDD as a wrapper for EventStore
// * @param <ID>
// * @param <EVENT>
// * @param <ROOT>
// */
//public class RepositoryOverEventStore<ID extends RootAggregateId, EVENT extends Event, ROOT extends RootAggregate<ID, EVENT>> implements Repository<ROOT> {
//
//	protected EventStore<ID, EVENT> eventStore;
//	private Class<ROOT> rootClass;
//
//	public RepositoryOverEventStore(EventStore<ID, EVENT> eventStore, Class<ROOT> rootClass) {
//
//		Contract.notNull(eventStore, "eventStore");
//		Contract.notNull(rootClass, "rootClass");
//
//		this.eventStore = eventStore;
//		// TODO [bilu] 22.10.17 switch direct Class usage in somthing more dynamic eg ParameterizedType
//		this.rootClass = rootClass;
//	}
//
//	private ROOT invokeConstructor(Class<ROOT> clazz, List<EVENT> events) {
//
//		try {
//			Constructor<ROOT> constructor = clazz.getConstructor(List.class);
//			return constructor.newInstance(events);
//		}
//		catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
//			throw new RuntimeException("Problem with invocation of " + clazz.getSimpleName() + " constructor", e);
//		}
//	}
//
//	@Override
//	public ROOT get(ID id) {
//
//		//		return new ROOT(eventStore.loadEvents(rootAggregateId));
//		return invokeConstructor(rootClass, eventStore.loadEvents(id));
//	}
//
//	@Override
//	public void save(ROOT rootAggregate) {
//
//		eventStore.appendEvents(rootAggregate.id(), rootAggregate.getUncommittedEvents(), rootAggregate.concurrencyVersion());
//		rootAggregate.clearUncommittedEvents();
//	}
//
//	@Override
//	public boolean exists(ID id) {
//
//		return eventStore.exists(id);
//	}
//}
