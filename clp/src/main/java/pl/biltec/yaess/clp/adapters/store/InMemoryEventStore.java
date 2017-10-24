//
//package pl.biltec.yaess.clp.adapters.store;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import pl.biltec.yaess.clp.domain.customer.RootAggregateId;
//import pl.biltec.yaess.clp.domain.customer.event.Event;
//import pl.biltec.yaess.core.common.exception.ConcurrentModificationException;
//import pl.biltec.yaess.core.adapters.store.EventStore;
//import pl.biltec.yaess.core.adapters.store.EventSubscriber;
//import pl.biltec.yaess.core.adapters.store.ExtendedEventSubscriber;
//import pl.biltec.yaess.core.common.Contract;
//
//
//public class InMemoryEventStore implements EventStore<RootAggregateId, Event> {
//
//	private List<Event> orderedEvent = Collections.synchronizedList(new LinkedList<>());
//	private Map<Class<? extends Event>, List<Object>> subscribers = Collections.synchronizedMap(new HashMap<>());
//	private Set<ExtendedEventSubscriber> subscribersExtended = Collections.synchronizedSet(new HashSet<>());
//	ExecutorService executor = Executors.newSingleThreadExecutor();
//
//	@Override
//	public List<Event> loadEvents(RootAggregateId RootAggregateId) {
//
//		Contract.notNull(RootAggregateId, "rootAggregateId");
//
//		return customerStream(RootAggregateId)
//			.collect(Collectors.toList());
//	}
//
//	@Override
//	public List<Event> loadEvents(RootAggregateId RootAggregateId, int skipEvents, int maxCount) {
//
//		return customerStream(RootAggregateId)
//			.skip(skipEvents)
//			.limit(maxCount)
//			.collect(Collectors.toList());
//	}
//
//	private Stream<Event> customerStream(RootAggregateId RootAggregateId) {
//
//		return orderedEvent.stream()
//			.filter(Event -> Event.rootAggregateId().equals(RootAggregateId));
//	}
//
//	@Override
//	public synchronized void appendEvents(RootAggregateId RootAggregateId, List<Event> events, long currentConcurrencyVersion) {
//
//		Contract.notNull(RootAggregateId, "RootAggregateId");
//		Contract.notNull(events, "events");
//		Contract.notNull(currentConcurrencyVersion, "currentConcurrencyVersion");
//
//		if (exists(RootAggregateId)) {
//			long calculatedConcurrencyVersion = customerStream(RootAggregateId).count();
//
//			if (calculatedConcurrencyVersion != (currentConcurrencyVersion - events.size())) {
//				throw new ConcurrentModificationException(RootAggregateId, currentConcurrencyVersion, calculatedConcurrencyVersion);
//			}
//		}
//
//		List<Event> toBePublished = new LinkedList<>();
//
//		events.forEach(
//			newEvent -> {
//				orderedEvent.add(newEvent);
//				toBePublished.add(newEvent);
//			}
//		);
//
//		//		Integer lastConcurrencyVersion = orderedEvent.stream().reduce((a, b) -> b).map(Event::getConcurrencyVersion).orElse(0);
//		//		IntWrapper actualConcurrencyVersion = new IntWrapper(lastConcurrencyVersion);
//		//		List<Event> toBePublished = new LinkedList<>();
//
//		//		if (actualConcurrencyVersion.isDifferentThan(currentConcurrencyVersion)) {
//		//			throw new ConcurrentModificationException(rootAggregateId, currentConcurrencyVersion, actualConcurrencyVersion.value());
//		//		}
//		//		events.forEach(
//		//			newEvent -> {
//		//				if (actualConcurrencyVersion.increaseByOne().isDifferentThan(newEvent.getConcurrencyVersion())) {
//		//					throw new ConcurrentModificationException(rootAggregateId, currentConcurrencyVersion, actualConcurrencyVersion.value());
//		//				}
//		//				orderedEvent.add(newEvent);
//		//				toBePublished.add(newEvent);
//		//			}
//		//		);
//
//		//async
//		executor.submit(() -> publish(toBePublished));
//		executor.submit(() -> publishExtended(toBePublished));
//		//sync
//		//		publish(toBePublished);
//		//		publishExtended(toBePublished);
//	}
//
//	private void publish(List<Event> toBePublished) {
//
//		toBePublished.stream()
//			.parallel()
//			.forEach(
//				event -> {
//					List<Object> EventSubscribers = subscribers.get(event.getClass());
//					if (customerEventSubscribers != null) {
//						customerEventSubscribers.forEach(subscriber -> ((EventSubscriber) subscriber).handleEvent(event));
//					}
//				}
//			);
//	}
//
//	private void publishExtended(List<CustomerEvent> toBePublished) {
//
//		toBePublished.stream()
//			.parallel()
//			.forEach(
//				event -> {
//					subscribersExtended.stream()
//						.filter(extendedEventSubscriber -> extendedEventSubscriber.supports(event))
//						.forEach(subscriberExtended -> subscriberExtended.handleEvent(event));
//				}
//			);
//	}
//
//	@Override
//	public void addEventSubscriber(EventSubscriber<RootAggregateId, CustomerEvent> eventSubscriber) {
//
//		// TODO: [pbilewic] 11.10.17 debilne, refaktor
//		List<Object> customerEventSubscribers = subscribers.get(eventSubscriber.eventType());
//		if (customerEventSubscribers == null) {
//			customerEventSubscribers = Arrays.asList(eventSubscriber);
//			subscribers.put(eventSubscriber.eventType(), customerEventSubscribers);
//		}
//		else {
//			customerEventSubscribers = new ArrayList<>(customerEventSubscribers);
//			customerEventSubscribers.add(eventSubscriber);
//			subscribers.put(eventSubscriber.eventType(), customerEventSubscribers);
//		}
//
//	}
//
//	@Override
//	public void addEventSubscriber(ExtendedEventSubscriber eventSubscriber) {
//
//		subscribersExtended.add(eventSubscriber);
//	}
//
//	@Override
//	public boolean exists(RootAggregateId RootAggregateId) {
//
//		return customerStream(RootAggregateId)
//			.findFirst()
//			.isPresent();
//	}
//
//}
