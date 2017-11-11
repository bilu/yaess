package pl.biltec.yaess.core.adapters.store.memory;

import java.time.LocalDateTime;


class EventRecord {

	private String rootAggregateName;
	private String rootId;

	private String eventClassName;
	private String eventAsJson;
	private LocalDateTime eventCreated;

	public EventRecord(String rootAggregateName, String rootId, String eventClassName, String eventAsJson, LocalDateTime eventCreated) {

		this.rootAggregateName = rootAggregateName;
		this.rootId = rootId;
		this.eventClassName = eventClassName;
		this.eventAsJson = eventAsJson;
		this.eventCreated = eventCreated;
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
