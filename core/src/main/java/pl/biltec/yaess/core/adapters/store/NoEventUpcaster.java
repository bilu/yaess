package pl.biltec.yaess.core.adapters.store;

import java.util.List;
import java.util.stream.Collectors;

import pl.biltec.yaess.core.domain.Event;


public class NoEventUpcaster implements EventUpcaster {

	@Override
	public List<Event> upcast(List<Event> events) {

		return events.stream().filter(
			event -> !event.getClass().getName().contains("ChangedEmail")
		).collect(Collectors.toList());

//		return events;
	}
}
