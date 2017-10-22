
package pl.biltec.yaess.clp.adapters.store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.biltec.yaess.clp.domain.customer.CustomerId;
import pl.biltec.yaess.core.adapters.store.EventStore;
import pl.biltec.yaess.clp.domain.customer.event.CustomerEvent;
import pl.biltec.yaess.clp.domain.customer.exception.ConcurrentModificationException;
import pl.biltec.yaess.core.adapters.store.EventSubscriber;
import pl.biltec.yaess.core.adapters.store.ExtendedEventSubscriber;
import pl.biltec.yaess.core.common.Contract;


public class InMemoryEventStore implements EventStore<CustomerId, CustomerEvent> {

	private List<CustomerEvent> orderedEvent = Collections.synchronizedList(new LinkedList<>());
	private Map<Class<? extends CustomerEvent>, List<Object>> subscribers = Collections.synchronizedMap(new HashMap<>());
	private Set<ExtendedEventSubscriber> subscribersExtended = Collections.synchronizedSet(new HashSet<>());
	ExecutorService executor = Executors.newSingleThreadExecutor();

	@Override
	public List<CustomerEvent> loadEvents(CustomerId customerId) {

		Contract.notNull(customerId, "rootAggregateId");

		return customerStream(customerId)
			.collect(Collectors.toList());
	}

	@Override
	public List<CustomerEvent> loadEvents(CustomerId customerId, int skipEvents, int maxCount) {

		return customerStream(customerId)
			.skip(skipEvents)
			.limit(maxCount)
			.collect(Collectors.toList());
	}

	private Stream<CustomerEvent> customerStream(CustomerId customerId) {

		return orderedEvent.stream()
			.filter(customerEvent -> customerEvent.rootAggregateId().equals(customerId));
	}

	@Override
	public synchronized void appendEvents(CustomerId customerId, List<CustomerEvent> events, long currentConcurrencyVersion) {

		Contract.notNull(customerId, "customerId");
		Contract.notNull(events, "events");
		Contract.notNull(currentConcurrencyVersion, "currentConcurrencyVersion");

		if (exists(customerId)) {
			long calculatedConcurrencyVersion = customerStream(customerId).count();

			if (calculatedConcurrencyVersion != (currentConcurrencyVersion - events.size())) {
				throw new ConcurrentModificationException(customerId, currentConcurrencyVersion, calculatedConcurrencyVersion);
			}
		}

		List<CustomerEvent> toBePublished = new LinkedList<>();

		events.forEach(
			newEvent -> {
				orderedEvent.add(newEvent);
				toBePublished.add(newEvent);
			}
		);

		//		Integer lastConcurrencyVersion = orderedEvent.stream().reduce((a, b) -> b).map(CustomerEvent::getConcurrencyVersion).orElse(0);
		//		IntWrapper actualConcurrencyVersion = new IntWrapper(lastConcurrencyVersion);
		//		List<CustomerEvent> toBePublished = new LinkedList<>();

		//		if (actualConcurrencyVersion.isDifferentThan(currentConcurrencyVersion)) {
		//			throw new ConcurrentModificationException(rootAggregateId, currentConcurrencyVersion, actualConcurrencyVersion.value());
		//		}
		//		events.forEach(
		//			newEvent -> {
		//				if (actualConcurrencyVersion.increaseByOne().isDifferentThan(newEvent.getConcurrencyVersion())) {
		//					throw new ConcurrentModificationException(rootAggregateId, currentConcurrencyVersion, actualConcurrencyVersion.value());
		//				}
		//				orderedEvent.add(newEvent);
		//				toBePublished.add(newEvent);
		//			}
		//		);

		//async
		executor.submit(() -> publish(toBePublished));
		executor.submit(() -> publishExtended(toBePublished));
		//sync
		//		publish(toBePublished);
		//		publishExtended(toBePublished);
	}

	private void publish(List<CustomerEvent> toBePublished) {

		toBePublished.stream()
			.parallel()
			.forEach(
				event -> {
					List<Object> customerEventSubscribers = subscribers.get(event.getClass());
					if (customerEventSubscribers != null) {
						customerEventSubscribers.forEach(subscriber -> ((EventSubscriber) subscriber).handleEvent(event));
					}
				}
			);
	}

	private void publishExtended(List<CustomerEvent> toBePublished) {

		toBePublished.stream()
			.parallel()
			.forEach(
				event -> {
					subscribersExtended.stream()
						.filter(extendedEventSubscriber -> extendedEventSubscriber.supports(event))
						.forEach(subscriberExtended -> subscriberExtended.handleEvent(event));
				}
			);
	}

	@Override
	public void addEventSubscriber(EventSubscriber<CustomerId, CustomerEvent> eventSubscriber) {

			// TODO: [pbilewic] 11.10.17 debilne, refaktor
		List<Object> customerEventSubscribers = subscribers.get(eventSubscriber.eventType());
		if (customerEventSubscribers == null) {
			customerEventSubscribers = Arrays.asList(eventSubscriber);
			subscribers.put(eventSubscriber.eventType(), customerEventSubscribers);
		}
		else {
			customerEventSubscribers = new ArrayList<>(customerEventSubscribers);
			customerEventSubscribers.add(eventSubscriber);
			subscribers.put(eventSubscriber.eventType(), customerEventSubscribers);
		}

	}

	@Override
	public void addEventSubscriber(ExtendedEventSubscriber eventSubscriber) {

		subscribersExtended.add(eventSubscriber);
	}

	@Override
	public boolean exists(CustomerId customerId) {

		return customerStream(customerId)
			.findFirst()
			.isPresent();
	}

	class IntWrapper {

		private int value;

		IntWrapper(int value) {

			this.value = value;
		}

		boolean isDifferentThan(int otherValue) {

			return value != otherValue;
		}

		IntWrapper increaseByOne() {

			++value;
			return this;
		}

		int value() {

			return value;
		}
	}

}
