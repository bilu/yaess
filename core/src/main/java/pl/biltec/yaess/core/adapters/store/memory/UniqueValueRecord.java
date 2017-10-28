package pl.biltec.yaess.core.adapters.store.memory;

import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class UniqueValueRecord {

	private Class<? extends RootAggregate> rootAggregateClass;
	private RootAggregateId rootAggregateId;
	private String attributeName;
	private String attributeValue;

	public UniqueValueRecord(Class<? extends RootAggregate> rootAggregateClass, RootAggregateId rootAggregateId, String attributeName, String attributeValue) {

		this.rootAggregateClass = rootAggregateClass;
		this.rootAggregateId = rootAggregateId;
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
	}

	public Class<? extends RootAggregate> getRootAggregateClass() {

		return rootAggregateClass;
	}

	public RootAggregateId getRootAggregateId() {

		return rootAggregateId;
	}

	public String getAttributeName() {

		return attributeName;
	}

	public String getAttributeValue() {

		return attributeValue;
	}
}
