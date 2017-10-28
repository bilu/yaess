package pl.biltec.yaess.core.adapters.store;

import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


public interface UniqueValuesStore {

	/**
	 *
	 * @param rootAggregateClass
	 * @param omittingRootAggregateId
	 * @param attributeName
	 * @param attributeValue
	 * @return true only when there is no attributeValue == email found for the same (rootAggregateClass, rootAggregateId, attributeName)
	 */
	boolean isUnique(Class<? extends RootAggregate> rootAggregateClass, RootAggregateId omittingRootAggregateId, String attributeName, String attributeValue);

	boolean isUnique(Class<? extends RootAggregate> rootAggregateClass, String attributeName, String attributeValue);

	void addUnique(Class<? extends RootAggregate> rootAggregateClass, RootAggregateId rootAggregateId, String attributeName, String attributeValue);

	// TODO [bilu] 28.10.17 custom made object
	//Map<attributeName, List<rootAggregateId>>
//	Map<String, List<RootAggregateId>> getNotUnique(Class<? extends RootAggregate> rootAggregateClass);
}
