package pl.biltec.yaess.core.adapters.store;

import java.util.List;

import pl.biltec.yaess.core.domain.Event;


public interface EventUpcaster {

	List<Event> upcast(List<Event> events);

}
