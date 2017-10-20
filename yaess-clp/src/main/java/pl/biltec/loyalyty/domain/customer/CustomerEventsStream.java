package pl.biltec.loyalyty.domain.customer;

import java.util.List;

import pl.biltec.loyalyty.domain.customer.event.CustomerEvent;


public class CustomerEventsStream {

	private int concurrencyVersion;
	private List<CustomerEvent> events;

	public CustomerEventsStream(int concurrencyVersion, List<CustomerEvent> events) {

		this.concurrencyVersion = concurrencyVersion;
		this.events = events;
	}

	public int getConcurrencyVersion() {

		return concurrencyVersion;
	}

	public List<CustomerEvent> getEvents() {

		return events;
	}
}
