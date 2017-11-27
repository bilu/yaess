package pl.biltec.yaess.core.adapters.store.jpa;

import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.biltec.yaess.core.adapters.store.UniqueValuesStore;
import pl.biltec.yaess.core.common.Contract;
import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


@Service
public class JpaUniqueValuesStore implements UniqueValuesStore {

	private UniqueValueRecordRepository uniqueValueRecordRepository;

	@Autowired
	public JpaUniqueValuesStore(UniqueValueRecordRepository uniqueValueRecordRepository) {

		this.uniqueValueRecordRepository = uniqueValueRecordRepository;
	}

	@Override
	public boolean isUnique(Class<? extends RootAggregate> rootAggregateClass, RootAggregateId omittingRootAggregateId, String attributeName, String attributeValue) {

		Contract.notNull(rootAggregateClass, "rootAggregateClass");
		Contract.notNull(omittingRootAggregateId, "omittingRootAggregateId");
		Contract.notNull(attributeName, "attributeName");
		Contract.notNull(attributeValue, "attributeValue");

//		return uniqueValueRecords.stream()
		return StreamSupport.stream(uniqueValueRecordRepository.findAll().spliterator(), false)
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

		return StreamSupport.stream(uniqueValueRecordRepository.findAll().spliterator(), false)
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

		uniqueValueRecordRepository.save(new UniqueValueRecord(rootAggregateClass, rootAggregateId, attributeName, attributeValue));
	}

	@Override
	public void removeUnique(Class<? extends RootAggregate> rootAggregateClass, RootAggregateId rootAggregateId, String attributeName, String attributeValue) {

		Contract.notNull(rootAggregateClass, "rootAggregateClass");
		Contract.notNull(attributeName, "attributeName");
		Contract.notNull(attributeValue, "attributeValue");

		StreamSupport.stream(uniqueValueRecordRepository.findAll().spliterator(), false)
			.filter(uniqueValueRecord -> uniqueValueRecord.getRootAggregateClass().equals(rootAggregateClass))
			.filter(uniqueValueRecord -> uniqueValueRecord.getAttributeName().equals(attributeName))
			.filter(uniqueValueRecord -> uniqueValueRecord.getAttributeValue().equals(attributeValue))
			.findFirst()
			.ifPresent(uniqueValueRecordRepository::delete);
	}
}
