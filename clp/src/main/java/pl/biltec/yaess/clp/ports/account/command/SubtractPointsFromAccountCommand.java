package pl.biltec.yaess.clp.ports.account.command;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.clp.ports.Command;


public class SubtractPointsFromAccountCommand extends Command {

	private int points;

	public SubtractPointsFromAccountCommand(String accountId, int points, String originator) {
		super(originator, accountId);
		this.points = points;
	}

	public int getPoints() {

		return points;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof SubtractPointsFromAccountCommand))
			return false;

		SubtractPointsFromAccountCommand that = (SubtractPointsFromAccountCommand) o;

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
			.append("originator", originator)
			.append("rootAggregateId", rootAggregateId)
			.toString();
	}
}
