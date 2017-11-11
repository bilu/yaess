package pl.biltec.yaess.core.adapters.store;

import java.util.HashMap;
import java.util.Map;

import pl.biltec.yaess.core.domain.Event;


public abstract class DeprecatedEventsUpcaster {

	private Map<Class<?>, EventConverter> upcaster = new HashMap<>();

	public <OLD_EVENT extends Event> void define(Class<OLD_EVENT> deprecatedEvent, EventConverter<OLD_EVENT> deprecatedEventConverter) {

		upcaster.put(deprecatedEvent, deprecatedEventConverter);
	}

	public interface  EventConverter<OLD_EVENT extends Event> {

		Event convert(OLD_EVENT oldEvent);
	}

	public boolean contains(Class<? extends Event> deprecatedEventClass) {

		return upcaster.containsKey(deprecatedEventClass);
	}

	public Event upcast(Class<? extends Event> deprecatedEventClass, Event oldEvent) {

		return upcaster.get(deprecatedEventClass).convert(oldEvent);
	}
}
