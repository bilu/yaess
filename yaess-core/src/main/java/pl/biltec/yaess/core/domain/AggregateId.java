package pl.biltec.yaess.core.domain;

import static pl.biltec.yaess.yaess.core.common.contract.Contract.notNull;

import java.util.Objects;
import java.util.UUID;

import pl.biltec.yaess.yaess.core.common.contract.Contract;


public abstract class AggregateId {

	private UUID id;

	public AggregateId() {

		this.id = UUID.randomUUID();
	}

	public AggregateId(UUID id) {

		this.id = Contract.notNull(id, "id");
	}

	public AggregateId(String id) {

		this.id = UUID.fromString(Contract.notNull(id, "id"));
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		AggregateId that = (AggregateId) o;
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
