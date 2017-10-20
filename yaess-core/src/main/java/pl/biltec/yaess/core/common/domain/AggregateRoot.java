package pl.biltec.yaess.core.common.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class AggregateRoot<ID extends AggregateId, EVENT extends AbstractEvent<ID>> {

	protected ID id;
	protected int concurrencyVersion = 0;
	protected List<EVENT> uncommittedEvents = new ArrayList<>();


	public ID id() {

		return id;
	}

	public int concurrencyVersion() {

		return concurrencyVersion;
	}

	public List<EVENT> getUncommittedEvents() {

		return Collections.unmodifiableList(uncommittedEvents);
	}

}
