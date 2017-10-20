package pl.biltec.loyalyty.domain.customer;

import static pl.biltec.es.common.contract.Contract.notNull;

import java.util.Objects;
import java.util.UUID;


// TODO: [pbilewic] 12.10.17 zasięg
public class CustomerId {

	private UUID id;

	public CustomerId() {

		this.id = UUID.randomUUID();
	}

	public CustomerId(UUID id) {

		this.id = notNull(id, "id");
	}

	public CustomerId(String id) {

		// TODO: [pbilewic] 12.10.17 co z wyjątkami
		this.id = UUID.fromString(notNull(id, "id"));
	}



	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		CustomerId that = (CustomerId) o;
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
