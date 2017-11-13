package pl.biltec.yaess.clp.domain.customer;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.clp.domain.customer.exception.CustomerNotExistsException;
import pl.biltec.yaess.clp.domain.event.CustomerCreatedV3Event;
import pl.biltec.yaess.clp.domain.event.CustomerDeletedEvent;
import pl.biltec.yaess.clp.domain.event.CustomerEmailChangedV2Event;
import pl.biltec.yaess.clp.domain.event.CustomerFirstNameChangedEvent;
import pl.biltec.yaess.clp.domain.event.CustomerLastNameChangedEvent;
import pl.biltec.yaess.core.common.Contract;
import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


/**
 * <pre>
 * Contains 3 parts:
 * 	1. State
 * 	2. Business Methods (invariants validation + events creation)
 * 	3. State mutation (consumes event + modify state)
 * </pre>
 *
 */
public class Customer extends RootAggregate {

	/* 1. Aggregate state */
	private boolean created;
	private boolean deleted;
	private String firstName;
	private String lastName;
	private String email;
	/** PESEL */
	private String personalIdNumber;
	private LocalDateTime creationTimestamp;
	private LocalDateTime lastUpdateTimestamp;

	public Customer(List<Event> events) {

		super(events);
	}

	/* 2. Aggregate business methods */
	public Customer(String customerId, String firstName, String lastName, String email, String personalIdNumber, String originator) {

		Contract.notNull(originator, "originator");
		Contract.notNull(firstName, "firstName");
		Contract.notNull(lastName, "lastName");
		Contract.notNull(email, "email");
		Contract.notNull(personalIdNumber, "personalIdNumber");
		apply(new CustomerCreatedV3Event(new RootAggregateId(customerId), firstName, lastName, email.toLowerCase(), personalIdNumber, LocalDateTime.now(), originator));
	}

	public void changeFirstName(String newFirstName, String originator) {

		Contract.notNull(originator, "originator");
		Contract.notNull(newFirstName, "newFirstName");
		if (!this.firstName.equals(newFirstName)) {
			apply(new CustomerFirstNameChangedEvent(id, newFirstName, LocalDateTime.now(), originator));
		}
	}

	public void changeLastName(String newLastName, String originator) {

		Contract.notNull(originator, "originator");
		Contract.notNull(newLastName, "newLastName");
		if (!this.lastName.equals(newLastName)) {
			apply(new CustomerFirstNameChangedEvent(id, newLastName, LocalDateTime.now(), originator));
		}
	}

	public void changeEmail(String newEmail, String originator) {

		Contract.notNull(originator, "originator");
		Contract.notNull(newEmail, "newEmail");
		String newEmailLowerCase = newEmail.toLowerCase();
		// TODO [bilu] 28.10.17 should i override existing value? i chose not to
		if (!this.email.equals(newEmailLowerCase)) {
			apply(new CustomerEmailChangedV2Event(id, email, newEmailLowerCase, LocalDateTime.now(), originator));
		}

	}

	public void delete(String originator) {

		Contract.notNull(originator, "originator");
		if (deleted) {
			throw new CustomerNotExistsException(id);
		}
		apply(new CustomerDeletedEvent(id, LocalDateTime.now(), originator));
	}

	/* 3. Aggregate state mutation*/
	private void mutate(CustomerDeletedEvent event) {

		firstName = firstName + "_REMOVED_" + event.created() + "_BY_" + event.originator();
		lastName = lastName + "_REMOVED_" + event.created() + "_BY_" + event.originator();
		deleted = true;
	}

	private void mutate(CustomerEmailChangedV2Event event) {

		this.email = event.getNewEmail();
	}

	private void mutate(CustomerCreatedV3Event event) {

		this.created = true;
		this.firstName = event.getFirstName();
		this.lastName = event.getLastName();
		this.email = event.getEmail();
		this.personalIdNumber = event.getPersonalIdNumber();
		this.id = event.rootAggregateId();
		this.creationTimestamp = event.created();
	}

	private void mutate(CustomerFirstNameChangedEvent event) {

		this.firstName = event.getFirstName();
		this.lastUpdateTimestamp = event.created();
	}

	private void mutate(CustomerLastNameChangedEvent event) {

		this.lastName = event.getLastName();
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
			.append(firstName, customer.firstName)
			.append(lastName, customer.lastName)
			.append(email, customer.email)
			.append(personalIdNumber, customer.personalIdNumber)
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
			.append(firstName)
			.append(lastName)
			.append(email)
			.append(personalIdNumber)
			.append(creationTimestamp)
			.append(lastUpdateTimestamp)
			.toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("created", created)
			.append("deleted", deleted)
			.append("firstName", firstName)
			.append("lastName", lastName)
			.append("email", email)
			.append("personalIdNumber", personalIdNumber)
			.append("creationTimestamp", creationTimestamp)
			.append("lastUpdateTimestamp", lastUpdateTimestamp)
			.append("id", id)
			.toString();
	}
}
