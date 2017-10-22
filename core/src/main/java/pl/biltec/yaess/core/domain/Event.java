package pl.biltec.yaess.core.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Event<ID extends RootAggregateId> implements Serializable {

	protected ID rootAggregateId;
	protected int version;
	protected LocalDateTime created;

	protected Event(ID rootAggregateId, LocalDateTime created) {
		this.rootAggregateId = rootAggregateId;
		this.created = created;
		this.version = 1;
	}

	public ID rootAggregateId() {

		return rootAggregateId;
	}

	public LocalDateTime created() {

		return created;
	}

	public int version() {

		return version;
	}

	/**
	 * Force to implement
	 * @return
	 */
	@Override
	abstract public String toString();
}

