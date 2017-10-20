package pl.biltec.yaess.core.common.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.biltec.yaess.core.common.contract.Contract;


public abstract class AggregateRoot<ID extends AggregateId, EVENT extends AbstractEvent<ID>> {

	protected ID id;
	protected int concurrencyVersion = 0;
	protected List<EVENT> uncommittedEvents = new ArrayList<>();

	public AggregateRoot(List<EVENT> events) {
		apply(events);
	}

	protected AggregateRoot() {
	}

	public ID id() {

		return id;
	}

	public int concurrencyVersion() {

		return concurrencyVersion;
	}

	public List<EVENT> getUncommittedEvents() {

		return Collections.unmodifiableList(uncommittedEvents);
	}

	public void apply(List<EVENT> events) {

		Contract.notNull(events, "events");
		events.forEach(this::mutateState);
	}


	protected void apply(EVENT event) {
		//mutate Aggregate for incoming business events
		mutateState(event);
		//put aside not commited events
		uncommittedEvents.add(event);
	}

	protected abstract void mutateState(EVENT event);
}
