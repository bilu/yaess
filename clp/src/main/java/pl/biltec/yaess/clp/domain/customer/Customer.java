package pl.biltec.yaess.clp.domain.customer;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.clp.domain.customer.exception.CustomerNotExistsException;
import pl.biltec.yaess.clp.domain.event.CustomerCreatedV2Event;
import pl.biltec.yaess.clp.domain.event.CustomerDeletedEvent;
import pl.biltec.yaess.clp.domain.event.CustomerEmailChangedV2Event;
import pl.biltec.yaess.clp.domain.event.CustomerFirstNameChangedEvent;
import pl.biltec.yaess.clp.domain.event.CustomerSurnameChangedEvent;
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
	private String surname;
	private String email;
	/** PESEL */
	private String personalIdNumber;
	private LocalDateTime creationTimestamp;
	private LocalDateTime lastUpdateTimestamp;

	public Customer(List<Event> events) {

		super(events);
	}

	/* 2. Aggregate business methods */
	public Customer(String customerId, String firstName, String surname, String email, String personalIdNumber, String originator) {

		Contract.notNull(originator, "originator");
		Contract.notNull(firstName, "firstName");
		Contract.notNull(surname, "surname");
		Contract.notNull(email, "email");
		Contract.notNull(personalIdNumber, "personalIdNumber");
		apply(new CustomerCreatedV2Event(new RootAggregateId(customerId), firstName, surname, email.toLowerCase(), personalIdNumber, LocalDateTime.now(), originator));
	}

	public void changeFirstName(String newFirstName, String originator) {

		Contract.notNull(originator, "originator");
		Contract.notNull(newFirstName, "newFirstName");
		if (!this.firstName.equals(newFirstName)) {
			apply(new CustomerFirstNameChangedEvent(id, newFirstName, LocalDateTime.now(), originator));
		}
	}

	public void changeSurname(String newSurname, String originator) {

		Contract.notNull(originator, "originator");
		Contract.notNull(newSurname, "newSurname");
		if (!this.surname.equals(newSurname)) {
			apply(new CustomerFirstNameChangedEvent(id, newSurname, LocalDateTime.now(), originator));
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
		surname = surname + "_REMOVED_" + event.created() + "_BY_" + event.originator();
		deleted = true;
	}

	private void mutate(CustomerEmailChangedV2Event event) {

		this.email = event.getNewEmail();
	}

	private void mutate(CustomerCreatedV2Event event) {

		this.created = true;
		this.firstName = event.getFirstName();
		this.surname = event.getSurname();
		this.email = event.getEmail();
		this.personalIdNumber = event.getPersonalIdNumber();
		this.id = event.rootAggregateId();
		this.creationTimestamp = event.created();
	}

	private void mutate(CustomerFirstNameChangedEvent event) {

		this.firstName = event.getFirstName();
		this.lastUpdateTimestamp = event.created();
	}

	private void mutate(CustomerSurnameChangedEvent event) {

		this.surname = event.getSurname();
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
			.append(surname, customer.surname)
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
			.append(surname)
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
			.append("surname", surname)
			.append("email", email)
			.append("personalIdNumber", personalIdNumber)
			.append("creationTimestamp", creationTimestamp)
			.append("lastUpdateTimestamp", lastUpdateTimestamp)
			.append("id", id)
			.toString();
	}
}
