package pl.biltec.loyalyty.domain.customer.event;

import java.time.LocalDateTime;

import pl.biltec.es.common.es.Event;
import pl.biltec.loyalyty.domain.customer.CustomerId;


public abstract class CustomerEvent implements Event {

	protected CustomerId customerId;
	protected int version;
	protected LocalDateTime created;

	protected CustomerEvent(CustomerId customerId, LocalDateTime created) {

		this.customerId = customerId;
		this.created = created;
		this.version = 1;
	}

	public CustomerId getCustomerId() {

		return customerId;
	}

	public LocalDateTime getCreated() {

		return created;
	}

	public int getVersion() {

		return version;
	}

	/**
	 * // TODO: [pbilewic] 08.10.17 wymuszam override dla klas ktore dziedzicza
	 * @return
	 */
	@Override
	abstract public String toString();
}
