package pl.biltec.yaess.clp.domain.customer;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.clp.domain.customer.exception.CustomerNotExistsException;
import pl.biltec.yaess.clp.domain.event.CustomerChangedEmailEvent;
import pl.biltec.yaess.clp.domain.event.CustomerCreatedEvent;
import pl.biltec.yaess.clp.domain.event.CustomerDeletedEvent;
import pl.biltec.yaess.clp.domain.event.CustomerRenamedEvent;
import pl.biltec.yaess.core.common.Contract;
import pl.biltec.yaess.core.common.exception.UnsupportedEventException;
import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


/**
 * Jeśli nie chcemy trzymać oddzielnie stanu oraz metod biznesowych Customera to należy pamiętać aby tylko zdarzenia (metody apply) mutowały stan.
 * Nie mutujemy stanu z poziomu metod biznesowych bo wystąpi problem przy odtwarzaniu.
 */
public class Customer extends RootAggregate {

	private boolean created;
	private boolean deleted;
	private String name;
	private String email;
	private LocalDateTime creationTimestamp;
	private LocalDateTime lastUpdateTimestamp;

	public Customer(List<Event> events) {

		super(events);
	}

	public Customer(String name, String email, String originator) {

		// TODO: [pbilewic] 09.10.17 czy to nie powinien być wyjątek klasy DomainOperationException?
		Contract.notNull(originator, "originator");
		Contract.notNull(name, "newName");
		Contract.notNull(email, "email");
		apply(new CustomerCreatedEvent(new RootAggregateId(), name, email, LocalDateTime.now(), originator));
	}

	public void rename(String newName, String originator) {

		Contract.notNull(originator, "originator");
		Contract.notNull(newName, "newName");
		// TODO [bilu] 28.10.17 should i override existing value? i chose not to
		if (!this.name.equals(newName)) {
			apply(new CustomerRenamedEvent(id, newName, LocalDateTime.now(), originator));
		}
	}

	public void changeEmail(String newEmail, String originator) {

		Contract.notNull(originator, "originator");
		Contract.notNull(newEmail, "newEmail");
		// TODO [bilu] 28.10.17 should i override existing value? i chose not to
		if (!this.email.equals(newEmail)) {
			apply(new CustomerChangedEmailEvent(id, newEmail, LocalDateTime.now(), originator));
		}

	}

	public void delete(String originator) {

		Contract.notNull(originator, "originator");
		if (deleted) {
			throw new CustomerNotExistsException(id);
		}
		apply(new CustomerDeletedEvent(id, LocalDateTime.now(), originator));
	}

	//ES Mutowanie stanu zdarzeniami
	protected void mutateState(Event event) {

		//tego będzie dużo??
		//     https://stackoverflow.com/questions/3935832/java-equivalent-to-c-sharp-dynamic-class-type

		// TODO: [pbilewic] 08.10.17 magic
		//		https://stackoverflow.com/questions/3935832/java-equivalent-to-c-sharp-dynamic-class-type
		if (event instanceof CustomerCreatedEvent) {
			mutate((CustomerCreatedEvent) event);
		}
		else if (event instanceof CustomerRenamedEvent) {
			mutate((CustomerRenamedEvent) event);
		}
		else if (event instanceof CustomerChangedEmailEvent) {
			mutate((CustomerChangedEmailEvent) event);
		}
		else if (event instanceof CustomerDeletedEvent) {
			mutate((CustomerDeletedEvent) event);
		}
		else {
			throw new UnsupportedEventException(event);
		}
		incrementConcurrencyVersion();
	}

	private void mutate(CustomerDeletedEvent event) {
		// TODO [bilu] 28.10.17 add originator
		name = name + "_REMOVED_" + event.created() + "_BY_" + event.originator();
		deleted = true;
	}

	private void mutate(CustomerChangedEmailEvent event) {

		this.email = event.getEmail();
	}

	private void mutate(CustomerCreatedEvent event) {

		this.created = true;
		this.name = event.getName();
		this.email = event.getEmail();
		this.id = event.rootAggregateId();
		this.creationTimestamp = event.created();
	}

	private void mutate(CustomerRenamedEvent event) {

		this.name = event.getNewName();
		this.lastUpdateTimestamp = event.created();
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof Customer))
			return false;

		Customer customer = (Customer) o;

		return new EqualsBuilder()
			.appendSuper(super.equals(o))
			.append(created, customer.created)
			.append(deleted, customer.deleted)
			.append(name, customer.name)
			.append(email, customer.email)
			.append(creationTimestamp, customer.creationTimestamp)
			.append(lastUpdateTimestamp, customer.lastUpdateTimestamp)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.appendSuper(super.hashCode())
			.append(created)
			.append(deleted)
			.append(name)
			.append(email)
			.append(creationTimestamp)
			.append(lastUpdateTimestamp)
			.toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("created", created)
			.append("deleted", deleted)
			.append("name", name)
			.append("email", email)
			.append("creationTimestamp", creationTimestamp)
			.append("lastUpdateTimestamp", lastUpdateTimestamp)
			.append("id", id)
			.append("uncommittedEvents", uncommittedEvents)
			.toString();
	}
}
