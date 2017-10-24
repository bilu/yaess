package pl.biltec.yaess.core.adapters.store.memory;

import java.time.LocalDateTime;


class EventRecord {

	private String rootAggregateName;
	private String rootId;

	private String eventName;
	private int eventVersion;
	// TODO [bilu] 22.10.17
//	private byte[] serializedEvent;
	private Object serializedEvent;
	private LocalDateTime eventCreated;

	public EventRecord(String rootAggregateName, String rootId, String eventName, int eventVersion, Object serializedEvent, LocalDateTime eventCreated) {

		this.rootAggregateName = rootAggregateName;
		this.rootId = rootId;
		this.eventName = eventName;
		this.eventVersion = eventVersion;
		this.serializedEvent = serializedEvent;
		this.eventCreated = eventCreated;
	}

	public String getRootAggregateName() {

		return rootAggregateName;
	}

	public String getRootId() {

		return rootId;
	}

	public String getEventName() {

		return eventName;
	}

	public int getEventVersion() {

		return eventVersion;
	}

	public Object getSerializedEvent() {

		return serializedEvent;
	}

	public LocalDateTime getEventCreated() {

		return eventCreated;
	}
}
