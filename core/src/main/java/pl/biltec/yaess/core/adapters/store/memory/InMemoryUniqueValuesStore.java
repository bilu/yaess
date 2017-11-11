package pl.biltec.yaess.core.adapters.store.memory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import pl.biltec.yaess.core.adapters.store.UniqueValuesStore;
import pl.biltec.yaess.core.common.Contract;
import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class InMemoryUniqueValuesStore implements UniqueValuesStore {

	private List<UniqueValueRecord> uniqueValueRecords = Collections.synchronizedList(new LinkedList<>());

	@Override
	public boolean isUnique(Class<? extends RootAggregate> rootAggregateClass, RootAggregateId omittingRootAggregateId, String attributeName, String attributeValue) {

		Contract.notNull(rootAggregateClass, "rootAggregateClass");
		Contract.notNull(omittingRootAggregateId, "omittingRootAggregateId");
		Contract.notNull(attributeName, "attributeName");
		Contract.notNull(attributeValue, "attributeValue");

		return uniqueValueRecords.stream()
			.filter(uniqueValueRecord -> uniqueValueRecord.getRootAggregateClass().equals(rootAggregateClass))
			.filter(uniqueValueRecord -> !uniqueValueRecord.getRootAggregateId().equals(omittingRootAggregateId))
			.filter(uniqueValueRecord -> uniqueValueRecord.getAttributeName().equals(attributeName))
			.filter(uniqueValueRecord -> uniqueValueRecord.getAttributeValue().equals(attributeValue))
			.count() == 0L;

	}

	@Override
	public boolean isUnique(Class<? extends RootAggregate> rootAggregateClass, String attributeName, String attributeValue) {

		Contract.notNull(rootAggregateClass, "rootAggregateClass");
		Contract.notNull(attributeName, "attributeName");
		Contract.notNull(attributeValue, "attributeValue");

		return uniqueValueRecords.stream()
			.filter(uniqueValueRecord -> uniqueValueRecord.getRootAggregateClass().equals(rootAggregateClass))
			.filter(uniqueValueRecord -> uniqueValueRecord.getAttributeName().equals(attributeName))
			.filter(uniqueValueRecord -> uniqueValueRecord.getAttributeValue().equals(attributeValue))
			.count() == 0L;
	}

	@Override
	public void addUnique(Class<? extends RootAggregate> rootAggregateClass, RootAggregateId rootAggregateId, String attributeName, String attributeValue) {

		Contract.notNull(rootAggregateClass, "rootAggregateClass");
		Contract.notNull(rootAggregateId, "rootAggregateId");
		Contract.notNull(attributeName, "attributeName");
		Contract.notNull(attributeValue, "attributeValue");

		uniqueValueRecords.add(new UniqueValueRecord(rootAggregateClass, rootAggregateId, attributeName, attributeValue));
	}

	@Override
	public void removeUnique(Class<? extends RootAggregate> rootAggregateClass, RootAggregateId rootAggregateId, String attributeName, String attributeValue) {

		Contract.notNull(rootAggregateClass, "rootAggregateClass");
		Contract.notNull(attributeName, "attributeName");
		Contract.notNull(attributeValue, "attributeValue");

		uniqueValueRecords.stream()
			.filter(uniqueValueRecord -> uniqueValueRecord.getRootAggregateClass().equals(rootAggregateClass))
			.filter(uniqueValueRecord -> uniqueValueRecord.getAttributeName().equals(attributeName))
			.filter(uniqueValueRecord -> uniqueValueRecord.getAttributeValue().equals(attributeValue))
			.findFirst()
			.ifPresent(uniqueValueRecords::remove);
	}
}
