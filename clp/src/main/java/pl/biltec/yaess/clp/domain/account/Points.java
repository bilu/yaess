package pl.biltec.yaess.clp.domain.account;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.biltec.yaess.core.common.Contract;


public class Points {

	private int numberOfPoints;

	public Points(int numberOfPoints) {

		Contract.isTrue(numberOfPoints >= 0, "numberOfPoints >= 0");
		this.numberOfPoints = numberOfPoints;
	}

	public Points add(Points pointsToBeAdded) {

		return new Points(numberOfPoints + pointsToBeAdded.numberOfPoints);
	}

	public Points subtract(Points pointsToBeSubtracted) {

		return new Points(numberOfPoints - pointsToBeSubtracted.numberOfPoints);
	}

	int getNumberOfPoints() {

		return numberOfPoints;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof Points))
			return false;

		Points points = (Points) o;

		return new EqualsBuilder()
			.append(numberOfPoints, points.numberOfPoints)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.append(numberOfPoints)
			.toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
			.append("numberOfPoints", numberOfPoints)
			.toString();
	}
}
