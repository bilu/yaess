package pl.biltec.yaess.core.domain;

import java.util.ConcurrentModificationException;


public interface Repository<ROOT extends RootAggregate> {

	ROOT get(RootAggregateId id);

	default ROOT get(RootAggregateId id, long concurrencyVersion) {

		ROOT order = get(id);
		if (order.concurrencyVersion() != concurrencyVersion) {
			throw new ConcurrentModificationException(id.toString());
		}
		return order;
	}

	void save(ROOT customer);

	boolean exists(RootAggregateId id);
}
