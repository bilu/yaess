package pl.biltec.yaess.core.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


public abstract class Event implements Serializable {

	protected RootAggregateId rootAggregateId;
	protected UUID eventID;
	protected int version;
	protected LocalDateTime created;


	//TEST
	public Event() {


	}


	protected Event(RootAggregateId rootAggregateId, LocalDateTime created) {

		this.rootAggregateId = rootAggregateId;
		this.created = created;
		this.eventID = UUID.randomUUID();
		this.version = 1;
	}

	public RootAggregateId rootAggregateId() {

		return rootAggregateId;
	}

	public LocalDateTime created() {

		return created;
	}

	public int version() {

		return version;
	}

	public String eventId() {

		return eventID.toString();
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof Event))
			return false;

		Event event = (Event) o;

		return new EqualsBuilder()
			.append(version, event.version)
			.append(rootAggregateId, event.rootAggregateId)
			.append(eventID, event.eventID)
			.append(created, event.created)
			.isEquals();
	}

	/**
	 * Force to implement
	 * @return
	 */
	@Override
	abstract public String toString();

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.append(rootAggregateId)
			.append(eventID)
			.append(version)
			.append(created)
			.toHashCode();
	}
}

