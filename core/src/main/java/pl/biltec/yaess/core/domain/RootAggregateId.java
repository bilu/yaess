package pl.biltec.yaess.core.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import pl.biltec.yaess.core.common.Contract;


public abstract class RootAggregateId implements Serializable {

	private UUID id;

	public RootAggregateId() {

		this.id = UUID.randomUUID();
	}

	public RootAggregateId(UUID id) {

		this.id = Contract.notNull(id, "rootAggregateId");
	}

	public RootAggregateId(String id) {

		this.id = UUID.fromString(Contract.notNull(id, "rootAggregateId"));
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		RootAggregateId that = (RootAggregateId) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id);
	}

	@Override
	public String toString() {

		return id.toString().toLowerCase();
	}

}
