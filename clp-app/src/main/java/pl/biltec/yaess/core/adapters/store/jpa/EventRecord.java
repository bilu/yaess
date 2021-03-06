package pl.biltec.yaess.core.adapters.store.jpa;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import pl.biltec.yaess.core.common.annotation.MustExist;


@Entity
public class EventRecord implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String rootAggregateName;
	private String rootId;

	private String eventClassName;
	@Column(columnDefinition = "text")
	private String eventAsJson;
	private LocalDateTime eventCreated;

	// TODO [bilu] 27.11.17 verify if it is still necessary
	@MustExist(reason = MustExist.Reason.MAPPING)
	public EventRecord() {

	}

	public EventRecord(String rootAggregateName, String rootId, String eventClassName, String eventAsJson, LocalDateTime eventCreated) {

		this.rootAggregateName = rootAggregateName;
		this.rootId = rootId;
		this.eventClassName = eventClassName;
		this.eventAsJson = eventAsJson;
		this.eventCreated = eventCreated;
	}

	public Long getId() {

		return id;
	}

	public String getRootAggregateName() {

		return rootAggregateName;
	}

	public String getRootId() {

		return rootId;
	}

	public String getEventClassName() {

		return eventClassName;
	}

	public String getEventAsJson() {

		return eventAsJson;
	}

	public LocalDateTime getEventCreated() {

		return eventCreated;
	}
}
