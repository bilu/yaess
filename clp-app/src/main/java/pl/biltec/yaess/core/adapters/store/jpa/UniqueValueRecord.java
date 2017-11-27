package pl.biltec.yaess.core.adapters.store.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import pl.biltec.yaess.core.common.annotation.MustExist;
import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


@Entity
public class UniqueValueRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Class<? extends RootAggregate> rootAggregateClass;
	private RootAggregateId rootAggregateId;
	private String attributeName;
	private String attributeValue;

	// TODO [bilu] 27.11.17 verify if it is still necessary
	@MustExist(reason = MustExist.Reason.MAPPING)
	public UniqueValueRecord() {

	}

	public UniqueValueRecord(Class<? extends RootAggregate> rootAggregateClass, RootAggregateId rootAggregateId, String attributeName, String attributeValue) {

		this.rootAggregateClass = rootAggregateClass;
		this.rootAggregateId = rootAggregateId;
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
	}

	public Long getId() {

		return id;
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
