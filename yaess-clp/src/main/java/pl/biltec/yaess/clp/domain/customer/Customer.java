package pl.biltec.yaess.clp.domain.customer;

import java.time.LocalDateTime;
import java.util.List;

import pl.biltec.yaess.clp.domain.customer.event.CustomerCreatedEvent;
import pl.biltec.yaess.clp.domain.customer.event.CustomerDeletedEvent;
import pl.biltec.yaess.clp.domain.customer.event.CustomerEvent;
import pl.biltec.yaess.clp.domain.customer.event.CustomerRenamedEvent;
import pl.biltec.yaess.clp.domain.customer.exception.CustomerNotExistsException;
import pl.biltec.yaess.clp.domain.customer.exception.UnsupportedEventException;
import pl.biltec.yaess.core.common.contract.Contract;
import pl.biltec.yaess.core.common.domain.AggregateRoot;


/**
 * Jeśli nie chcemy trzymać oddzielnie stanu oraz metod biznesowych Customera to należy pamiętać aby tylko zdarzenia (metody apply) mutowały stan.
 * Nie mutujemy stanu z poziomu metod biznesowych bo wystąpi problem przy odtwarzaniu.
 */
public class Customer extends AggregateRoot<CustomerId, CustomerEvent> {

	//DOMAIN attributes
	private boolean created;
	private boolean deleted;
	private String name;
	private LocalDateTime creationTimestamp;
	private LocalDateTime lastUpdateTimestamp;


	public Customer(List<CustomerEvent> events) {
		super(events);
	}

	public Customer(String name) {
		// TODO: [pbilewic] 09.10.17 czy to nie powinien być wyjątek klasy DomainOperationException?
		Contract.notNull(name, "newName");
		apply(new CustomerCreatedEvent(new CustomerId(), name, LocalDateTime.now()));
	}


	public void rename(String newName) {

		Contract.notNull(newName, "newName");
		// TODO: [pbilewic] 08.10.17 czy tu potrzebuję sprawdzać czy jest utworzony?
		if (!name.equals(newName)) {
			apply(new CustomerRenamedEvent(id, newName, LocalDateTime.now()));
		}
	}

	public void delete() {

		if (deleted) {
			throw new CustomerNotExistsException(id);
		}
		//czy powinienem wywołać rename() czy stworzyć event?!
		LocalDateTime now = LocalDateTime.now();
		rename(name + "_USUNIĘTY_" + now);
		apply(new CustomerDeletedEvent(id, now));
	}

	//ES Mutowanie stanu zdarzeniami
	protected void mutateState(CustomerEvent event) {

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
		else {
			throw new UnsupportedEventException(event);
		}
		concurrencyVersion++;
	}

	private void mutate(CustomerCreatedEvent event) {

		this.created = true;
		this.name = event.getName();
		this.id = event.id();
		this.creationTimestamp = event.created();
	}

	private void mutate(CustomerRenamedEvent event) {

		this.name = event.getNewName();
		this.lastUpdateTimestamp = event.created();
	}
}
