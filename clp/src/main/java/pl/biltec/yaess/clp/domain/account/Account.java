package pl.biltec.yaess.clp.domain.account;

import java.time.LocalDateTime;
import java.util.List;

import pl.biltec.yaess.clp.domain.event.AccountCreatedEvent;
import pl.biltec.yaess.clp.domain.event.AccountDeactivatedEvent;
import pl.biltec.yaess.clp.domain.event.AccountPointsAddedEvent;
import pl.biltec.yaess.clp.domain.event.AccountPointsSubtractedEvent;
import pl.biltec.yaess.core.common.Contract;
import pl.biltec.yaess.core.domain.Event;
import pl.biltec.yaess.core.domain.RootAggregate;
import pl.biltec.yaess.core.domain.RootAggregateId;


/**
 * <pre>
 * Contains 3 parts:
 * 	1. State
 * 	2. Business Methods (invariants validation + events creation)
 * 	3. State mutation (consumes event + modify state)
 * </pre>
 *
 */
public class Account extends RootAggregate {

	/* 1. Aggregate state */
	private Points totalPoints;
	private boolean active;
	private String customerId;

	public Account(List<Event> events) {

		super(events);
	}

	/* 2. Aggregate business methods */
	public Account(RootAggregateId accountId, String customerId, Points totalPoints, String originator) {

		Contract.notNull(customerId, "customerId");
		Contract.notNull(totalPoints, "totalPoints");

		apply(new AccountCreatedEvent(accountId, customerId, totalPoints.getNumberOfPoints(), LocalDateTime.now(), originator));

	}

	public void addPoints(Points pointsToBeAdded, String originator) {

		Contract.notNull(pointsToBeAdded, "pointsToBeAdded");
		Contract.isTrue(active, "Account " + id + " is not active");
		apply(new AccountPointsAddedEvent(id, pointsToBeAdded.getNumberOfPoints(), LocalDateTime.now(), originator));
	}

	public void subtractPoints(Points pointsToBeSubtract, String originator) {

		Contract.notNull(pointsToBeSubtract, "pointsToBeSubtract");
		Contract.isTrue(active, "Account " + id + " is not active");
		totalPoints.subtract(pointsToBeSubtract); //validation - throws exception when illegal subtraction
		apply(new AccountPointsSubtractedEvent(id, pointsToBeSubtract.getNumberOfPoints(), LocalDateTime.now(), originator));
	}

	public void deactivate(String originator) {

		apply(new AccountDeactivatedEvent(id, LocalDateTime.now(), originator));
	}

	/* 3. Aggregate state mutation */
	private void mutate(AccountCreatedEvent event) {

		this.customerId = event.getCustomerId();
		this.totalPoints = new Points(event.getTotalPoints());
		this.active = true;
	}

	private void mutate(AccountPointsAddedEvent event) {

		this.totalPoints = totalPoints.add(new Points(event.getPoints()));
	}

	private void mutate(AccountPointsSubtractedEvent event) {

		this.totalPoints = totalPoints.subtract(new Points(event.getPoints()));
	}

	private void mutate(AccountDeactivatedEvent event) {
		this.active = false;
	}

}
