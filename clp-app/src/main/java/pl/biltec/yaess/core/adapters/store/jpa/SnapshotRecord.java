package pl.biltec.yaess.core.adapters.store.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import pl.biltec.yaess.core.common.annotation.MustExist;


@Entity
public class SnapshotRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String rootAggregateId;
	private String rootAggregateClassName;
	@Column(columnDefinition = "text")
	private String rootAggregateAsJson;

	// TODO [bilu] 27.11.17 verify if it is still necessary
	@MustExist(reason = MustExist.Reason.MAPPING)
	public SnapshotRecord() {

	}

	public SnapshotRecord(String rootAggregateId, String rootAggregateClassName, String rootAggregateAsJson) {

		this.rootAggregateId = rootAggregateId;
		this.rootAggregateClassName = rootAggregateClassName;
		this.rootAggregateAsJson = rootAggregateAsJson;
	}

	public Long getId() {

		return id;
	}

	public String getRootAggregateId() {

		return rootAggregateId;
	}

	public String getRootAggregateClassName() {

		return rootAggregateClassName;
	}

	public String getRootAggregateAsJson() {

		return rootAggregateAsJson;
	}
}
