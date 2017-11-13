package pl.biltec.yaess.core.adapters.store;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pl.biltec.yaess.core.common.Contract;
import pl.biltec.yaess.core.domain.Event;


public abstract class DeprecatedEventsUpcaster {

	private Map<Class<?>, EventConverter> upcaster = new HashMap<>();

	public <OLD_EVENT extends Event> void define(Class<OLD_EVENT> deprecatedEvent, EventConverter<OLD_EVENT> deprecatedEventConverter) {

		upcaster.put(deprecatedEvent, deprecatedEventConverter);
	}

	public interface EventConverter<OLD_EVENT extends Event> {

		List<Event> convert(OLD_EVENT oldEvent);
	}

	public boolean contains(Class<? extends Event> deprecatedEventClass) {

		return upcaster.containsKey(deprecatedEventClass);
	}

	public List<Event> upcast(Class<? extends Event> deprecatedEventClass, Event oldEvent) {

		Contract.notNull(deprecatedEventClass, "deprecatedEventClass");
		Contract.notNull(oldEvent, "oldEvent");

		List<Event> firstLevelConverted = upcaster.get(deprecatedEventClass).convert(oldEvent);

		//recurent upcasting for deprecated events on level 2 and more
		return firstLevelConverted.stream()
			.map(event -> {
				Class<? extends Event> eventClass = event.getClass();
				if (contains(eventClass)) {
					return upcast(eventClass, event);
				}
				return Arrays.asList(event);
			})
			.flatMap(List::stream)
			.collect(Collectors.toList());
	}
}
