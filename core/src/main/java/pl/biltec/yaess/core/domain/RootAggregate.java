package pl.biltec.yaess.core.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import pl.biltec.yaess.core.common.Contract;


public abstract class RootAggregate implements Serializable {

	protected RootAggregateId id;
	/**
	 * Not accessible to edit
	 */
	private long concurrencyVersion = 0;
	protected List<Event> uncommittedEvents = new ArrayList<>();

	public RootAggregate(List<Event> events) {

		apply(events);
	}

	protected RootAggregate() {

	}

	public RootAggregateId id() {

		return id;
	}

	public long concurrencyVersion() {

		return concurrencyVersion;
	}

	/**
	 * Only implementation can increment concurrency version (with every event)
	 */
	protected void incrementConcurrencyVersion() {

		this.concurrencyVersion++;
	}

	public List<Event> getUncommittedEvents() {

		return Collections.unmodifiableList(uncommittedEvents);
	}

	/**
	 * Should be invoked right after save action
	 */
	public void clearUncommittedEvents() {

		uncommittedEvents.clear();
	}

	public void apply(List<Event> events) {

		Contract.notNull(events, "events");
		events.stream()
			.peek(event -> {
					if (id != null) {
						boolean eventIdMatchRootAggreagteId = (id == event.rootAggregateId());
						Contract.isTrue(eventIdMatchRootAggreagteId, String.format("Event rootAggregateId=%s not match RootAggregate rootAggregateId=%s", id, event.rootAggregateId()));
					}
				}
			)
			.forEach(this::mutateState);
	}

	protected void apply(Event event) {

		//mutate Aggregate for incoming business events
		mutateState(event);
		//put aside not committed events
		uncommittedEvents.add(event);
	}

	protected abstract void mutateState(Event event);

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof RootAggregate))
			return false;

		RootAggregate that = (RootAggregate) o;

		return new EqualsBuilder()
			.append(concurrencyVersion, that.concurrencyVersion)
			.append(id, that.id)
			.append(uncommittedEvents, that.uncommittedEvents)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.append(id)
			.append(concurrencyVersion)
			.append(uncommittedEvents)
			.toHashCode();
	}
}
