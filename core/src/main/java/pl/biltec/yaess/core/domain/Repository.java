package pl.biltec.yaess.core.domain;

import java.util.ConcurrentModificationException;


public interface Repository<ID extends AggregateId, EVENT extends AbstractEvent<ID>, ROOT extends RootAggregate<ID, EVENT>> {

	ROOT get(ID id);

	default ROOT get(ID id, long concurrencyVersion) {

		ROOT order = get(id);
		if (order.concurrencyVersion() != concurrencyVersion) {
			throw new ConcurrentModificationException(id.toString());
		}
		return order;
	}

	void save(ROOT customer);

	boolean exists(ID id);
}
