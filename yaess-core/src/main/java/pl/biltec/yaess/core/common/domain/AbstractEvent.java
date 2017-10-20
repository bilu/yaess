package pl.biltec.yaess.core.common.domain;

import java.time.LocalDateTime;

public abstract class AbstractEvent<ID extends AggregateId> implements Event<ID> {

	protected ID id;
	protected int version;
	protected LocalDateTime created;

	protected AbstractEvent(ID id, LocalDateTime created) {
		this.id = id;
		this.created = created;
		this.version = 1;
	}

	public ID id() {

		return id;
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

