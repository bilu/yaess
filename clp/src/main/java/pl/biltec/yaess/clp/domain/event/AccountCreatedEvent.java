package pl.biltec.yaess.clp.domain.event;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregateId;


public class AccountCreatedEvent extends Event {

	private String customerId;
	private int totalPoints;

	public AccountCreatedEvent(RootAggregateId accountId, String customerId, int totalPoints, LocalDateTime created, String originator) {
		super(accountId, created, originator);

		this.customerId = customerId;
		this.totalPoints = totalPoints;
	}

	public String getCustomerId() {

		return customerId;
	}

	public int getTotalPoints() {

		return totalPoints;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof AccountCreatedEvent))
			return false;

		AccountCreatedEvent that = (AccountCreatedEvent) o;

		return new EqualsBuilder()
			.appendSuper(super.equals(o))
			.append(customerId, that.customerId)
			.append(totalPoints, that.totalPoints)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.appendSuper(super.hashCode())
			.append(customerId)
			.append(totalPoints)
			.toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("customerId", customerId)
			.append("totalPoints", totalPoints)
			.append("rootAggregateId", rootAggregateId)
			.append("eventId", eventId)
			.append("created", created)
			.append("originator", originator)
			.toString();
	}
}
