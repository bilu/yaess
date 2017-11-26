package pl.biltec.yaess.clp.ports;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import pl.biltec.yaess.core.common.Contract;


public abstract class Command {

	protected String originator;
	protected String rootAggregateId;

	public Command(String originator, String rootAggregateId) {

		Contract.notNull(originator, "originator");
		Contract.notNull(rootAggregateId, "rootAggregateId");

		this.originator = originator;
		this.rootAggregateId = rootAggregateId;
	}

	public String getOriginator() {

		return originator;
	}

	public String getRootAggregateId() {

		return rootAggregateId;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof Command))
			return false;

		Command that = (Command) o;

		return new EqualsBuilder()
			.append(originator, that.originator)
			.append(rootAggregateId, that.rootAggregateId)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.append(originator)
			.append(rootAggregateId)
			.toHashCode();
	}
}
