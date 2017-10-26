package pl.biltec.yaess.core.adapters.store.memory;

import java.time.LocalDateTime;


class EventRecord {

	private String rootAggregateName;
	private String rootId;

	private String eventClassName;
	private int eventVersion;
	private String eventAsJson;
	private LocalDateTime eventCreated;

	public EventRecord(String rootAggregateName, String rootId, String eventClassName, int eventVersion, String eventAsJson, LocalDateTime eventCreated) {

		this.rootAggregateName = rootAggregateName;
		this.rootId = rootId;
		this.eventClassName = eventClassName;
		this.eventVersion = eventVersion;
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

	public int getEventVersion() {

		return eventVersion;
	}

	public String getEventAsJson() {

		return eventAsJson;
	}

	public LocalDateTime getEventCreated() {

		return eventCreated;
	}
}
