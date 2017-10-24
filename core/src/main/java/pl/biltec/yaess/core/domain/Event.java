package pl.biltec.yaess.core.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


public abstract class Event implements Serializable {

	protected RootAggregateId rootAggregateId;
	protected UUID eventID;
	protected int version;
	protected LocalDateTime created;

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

	/**
	 * Force to implement
	 * @return
	 */
	@Override
	abstract public String toString();
}

