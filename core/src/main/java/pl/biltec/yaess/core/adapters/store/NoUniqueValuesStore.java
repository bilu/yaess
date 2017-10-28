package pl.biltec.yaess.core.adapters.store;

import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


/**
 * Throw IllegalStateException when used to avoid illusion of proper uniqueness validation
 */
public class NoUniqueValuesStore implements UniqueValuesStore {

	@Override
	public boolean isUnique(Class<? extends RootAggregate> rootAggregateClass, RootAggregateId omittingRootAggregateId, String attributeName, String attributeValue) {

		return throwIllegalStateException();
	}

	@Override
	public boolean isUnique(Class<? extends RootAggregate> rootAggregateClass, String attributeName, String attributeValue) {

		return throwIllegalStateException();
	}

	private <T> T throwIllegalStateException() {

		throw new IllegalStateException("Missing " + UniqueValuesStore.class + " implementation to handle uniqueness");
	}

	@Override
	public void addUnique(Class<? extends RootAggregate> rootAggregateClass, RootAggregateId rootAggregateId, String attributeName, String attributeValue) {

		throwIllegalStateException();

	}

//	@Override
//	public Map<String, List<RootAggregateId>> getNotUnique(Class<? extends RootAggregate> rootAggregateClass) {
//
//		return throwIllegalStateException();
//	}
}
