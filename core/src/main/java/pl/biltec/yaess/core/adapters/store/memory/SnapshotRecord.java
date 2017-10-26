package pl.biltec.yaess.core.adapters.store.memory;

class SnapshotRecord {

	private String rootAggregateClassName;
	private String rootAggregateAsJson;

	public SnapshotRecord(String rootAggregateClassName, String rootAggregateAsJson) {

		this.rootAggregateClassName = rootAggregateClassName;
		this.rootAggregateAsJson = rootAggregateAsJson;
	}

	public String getRootAggregateClassName() {

		return rootAggregateClassName;
	}

	public String getRootAggregateAsJson() {

		return rootAggregateAsJson;
	}
}
