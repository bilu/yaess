package pl.biltec.yaess.core.adapters.store;

import pl.biltec.yaess.core.domain.Event;


public abstract class SingleEventSubscriber<EVENT extends Event> implements EventSubscriber {

	private Class<EVENT> clazz;

	public SingleEventSubscriber(Class<EVENT> clazz) {

		this.clazz = clazz;
	}

	@Override
	public void handleEvent(Event event) {

		handle(clazz.cast(event));

	}

	public abstract void handle(EVENT cast);

	@Override
	public boolean supports(Event event) {

		return clazz.isInstance(event);
	}
}
