
package pl.biltec.loyalyty.infrastructure;

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

import pl.biltec.es.common.contract.Contract;
import pl.biltec.loyalyty.domain.customer.CustomerEventStore;
import pl.biltec.loyalyty.application.customer.CustomerEventSubscriber;
import pl.biltec.loyalyty.application.customer.CustomerEventSubscriberExtended;
import pl.biltec.loyalyty.domain.customer.CustomerEventsStream;
import pl.biltec.loyalyty.domain.customer.event.CustomerEvent;
import pl.biltec.loyalyty.domain.customer.CustomerId;
import pl.biltec.loyalyty.domain.customer.exception.ConcurrentModificationException;


public class InMemoryCustomerEventStore implements CustomerEventStore {

	private List<CustomerEvent> orderedEvent = Collections.synchronizedList(new LinkedList<>());
	private Map<Class<? extends CustomerEvent>, List<Object>> subscribers = Collections.synchronizedMap(new HashMap<>());
	private Set<CustomerEventSubscriberExtended> subscribersExtended = Collections.synchronizedSet(new HashSet<>());
	ExecutorService executor = Executors.newSingleThreadExecutor();

	@Override
	public CustomerEventsStream loadEvents(CustomerId customerId) {

		Contract.notNull(customerId, "customerId");

		List<CustomerEvent> events = customerStream(customerId)
			.collect(Collectors.toList());

		return new CustomerEventsStream(events.size(), events);
	}

	@Override
	public CustomerEventsStream loadEvents(CustomerId customerId, int skipEvents, int maxCount) {

		List<CustomerEvent> events = customerStream(customerId)
			.skip(skipEvents)
			.limit(maxCount)
			.collect(Collectors.toList());

		return new CustomerEventsStream(skipEvents + events.size(), events);
	}

	private Stream<CustomerEvent> customerStream(CustomerId customerId) {

		return orderedEvent.stream()
			.filter(customerEvent -> customerEvent.getCustomerId().equals(customerId));
	}

	@Override
	public synchronized void appendEvents(CustomerId customerId, List<CustomerEvent> events, int expectedConcurrencyVersion) {

		int calculatedConcurrencyVersion = (int) customerStream(customerId)
			.count();
		if (calculatedConcurrencyVersion != expectedConcurrencyVersion) {
			throw new ConcurrentModificationException(customerId, expectedConcurrencyVersion, calculatedConcurrencyVersion);
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

		//		if (actualConcurrencyVersion.isDifferentThan(expectedConcurrencyVersion)) {
		//			throw new ConcurrentModificationException(customerId, expectedConcurrencyVersion, actualConcurrencyVersion.value());
		//		}
		//		events.forEach(
		//			newEvent -> {
		//				if (actualConcurrencyVersion.increaseByOne().isDifferentThan(newEvent.getConcurrencyVersion())) {
		//					throw new ConcurrentModificationException(customerId, expectedConcurrencyVersion, actualConcurrencyVersion.value());
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
						customerEventSubscribers.forEach(subscriber -> ((CustomerEventSubscriber) subscriber).handleEvent(event));
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
						.filter(customerEventSubscriberExtended -> customerEventSubscriberExtended.supports(event))
						.forEach(subscriberExtended -> subscriberExtended.handleEvent(event));
				}
			);
	}

	@Override
	public void addEventSubscriber(CustomerEventSubscriber<? extends CustomerEvent> eventSubscriber) {

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
	public void addEventSubscriber(CustomerEventSubscriberExtended eventSubscriber) {

		subscribersExtended.add(eventSubscriber);
	}

	@Override
	public boolean alreadyExists(CustomerId customerId) {

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
