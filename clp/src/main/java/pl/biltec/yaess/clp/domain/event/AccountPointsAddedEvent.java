package pl.biltec.yaess.clp.domain.event;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class AccountPointsAddedEvent extends Event {

	private int points;

	public AccountPointsAddedEvent(RootAggregateId id, int points, LocalDateTime created, String originator) {
		super(id, created, originator);

		this.points = points;
	}

	public int getPoints() {

		return points;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof AccountPointsAddedEvent))
			return false;

		AccountPointsAddedEvent that = (AccountPointsAddedEvent) o;

		return new EqualsBuilder()
			.appendSuper(super.equals(o))
			.append(points, that.points)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.appendSuper(super.hashCode())
			.append(points)
			.toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("points", points)
			.append("rootAggregateId", rootAggregateId)
			.append("eventId", eventId)
			.append("created", created)
			.append("originator", originator)
			.toString();
	}
}
