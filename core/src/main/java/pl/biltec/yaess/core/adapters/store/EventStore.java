//package pl.biltec.yaess.core.adapters.store;
//
//import java.util.List;
//
//import pl.biltec.yaess.core.domain.Event;
//import pl.biltec.yaess.core.domain.RootAggregateId;
//
//
//public interface EventStore<ID extends RootAggregateId, EVENT extends Event> {
//
//	boolean exists(ID id);
//	List<EVENT> loadEvents(ID id);
//	List<EVENT> loadEvents(ID id, int skipEvents, int maxCount);
//	void appendEvents(ID id, List<EVENT> events, long expectedConcurrencyVersion);
//
//	// TODO [bilu] 24.10.17 repository responsibility, not event store
//	void addEventSubscriber(EventSubscriber<ID, EVENT> eventSubscriber);
//	void addEventSubscriber(ExtendedEventSubscriber eventSubscriberExtended);
//
//}
//
