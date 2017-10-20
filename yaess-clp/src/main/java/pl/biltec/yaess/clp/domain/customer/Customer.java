package pl.biltec.yaess.clp.domain.customer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import pl.biltec.yaess.clp.domain.customer.event.CustomerDeletedEvent;
import pl.biltec.yaess.clp.domain.customer.event.CustomerEvent;
import pl.biltec.yaess.clp.domain.customer.exception.CustomerNotExistsException;
import pl.biltec.yaess.yaess.core.common.contract.Contract;
import pl.biltec.yaess.clp.domain.customer.event.CustomerCreatedEvent;
import pl.biltec.yaess.clp.domain.customer.event.CustomerRenamedEvent;
import pl.biltec.yaess.clp.domain.customer.exception.UnsupportedEventException;


/**
 * Jeśli nie chcemy trzymać oddzielnie stanu oraz metod biznesowych Customera to należy pamiętać aby tylko zdarzenia (metody apply) mutowały stan.
 * Nie mutujemy stanu z poziomu metod biznesowych bo wystąpi problem przy odtwarzaniu.
 */
public class Customer {

	//DOMAIN attributes
	private boolean created;
	private boolean deleted;
	private CustomerId customerId;
	private String name;
	private LocalDateTime creationTimestamp;
	private LocalDateTime lastUpdateTimestamp;
	//wartość startowa musi być zgodna z imp EventStore
	private int concurrencyVersion = 0;

	//ES attributes
	//nowe zmiany do zakomitowania
	private List<CustomerEvent> uncommittedEvents = new ArrayList<>();

	public Customer(List<CustomerEvent> events) {

		apply(events);
	}

	// TODO: [pbilewic] 12.10.17 co z tym zasięgiem
	//potrzebne do różnicy między snapshotem a ostatnim eventem
	public void apply(List<CustomerEvent> events) {

		Contract.notNull(events, "events");
		events.forEach(this::mutateState);
	}

	public Customer(String name) {
		// TODO: [pbilewic] 09.10.17 czy to nie powinien być wyjątek klasy DomainOperationException?
		Contract.notNull(name, "newName");
		apply(new CustomerCreatedEvent(new CustomerId(), name, LocalDateTime.now()));
	}

	public CustomerId getId() {

		return customerId;
	}

	public List<CustomerEvent> getUncommittedEvents() {

		// TODO: [pbilewic] 08.10.17 wyciekanie referencji
		return uncommittedEvents;
	}

	void apply(CustomerEvent event) {
		//modyfikuj Agregat w oparciu o zdarzenia biznesowe
		mutateState(event);
		//odkładaj zdarzenia do zapisania w store
		uncommittedEvents.add(event);
	}

	public void rename(String newName) {

		Contract.notNull(newName, "newName");
		// TODO: [pbilewic] 08.10.17 czy tu potrzebuję sprawdzać czy jest utworzony?
		if (!name.equals(newName)) {
			apply(new CustomerRenamedEvent(customerId, newName, LocalDateTime.now()));
		}
	}

	public void delete() {

		if (deleted) {
			throw new CustomerNotExistsException(customerId);
		}
		//czy powinienem wywołać rename() czy stworzyć event?!
		LocalDateTime now = LocalDateTime.now();
		rename(name + "_USUNIĘTY_" + now);
		apply(new CustomerDeletedEvent(customerId, now));
	}

	//ES Mutowanie stanu zdarzeniami
	private void mutateState(CustomerEvent event) {

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

	public int getConcurrencyVersion() {

		return concurrencyVersion;
	}

	private void mutate(CustomerCreatedEvent event) {

		this.created = true;
		this.name = event.getName();
		this.customerId = event.id();
		this.creationTimestamp = event.created();
	}

	private void mutate(CustomerRenamedEvent event) {

		this.name = event.getNewName();
		this.lastUpdateTimestamp = event.created();
	}
}
