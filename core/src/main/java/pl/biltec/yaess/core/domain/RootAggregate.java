package pl.biltec.yaess.core.domain;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import pl.biltec.yaess.core.common.Contract;


/**
 * <pre>
 * Goal:
 * 	* Boilerplate out of the box
 * 		* concurrencyVersion for optimistic locking
 * 		* uncommittedEvents
 * 		* control Event to appropriate <b>mutate </b> method, not easy in java
 * 	* Speed up delivery
 * Contract:
 * 	* Keep state of Root Aggregate inside Root Aggregate
 * 	* Business method is the place to make:
 * 		* invariants validation
 * 		* events creation and applying via <b>apply</b> method
 * 	* Each event should have corresponding private <b>mutate</b> method defined in implementation
 * 	* Mutate method is the place to make:
 * 		* Root Aggregate state change
 * 	* Invocation of proper <b>mutate</b> method is dispatched in background
 * Other:
 * 	* Part related with mutate method invocation is based on Vaughn Vernon IDDD book
 * </pre>
 *
 */
public abstract class RootAggregate implements Serializable {

	private static final String MUTATOR_METHOD_NAME = "mutate";
	private static Map<String, Method> mutatorMethods = new HashMap<>();

	protected RootAggregateId id;
	/** Deliberately not accessible */
	private long concurrencyVersion;
	/** Deliberately not accessible */
	private List<Event> uncommittedEvents;

	public RootAggregate(List<Event> events) {

		this();
		apply(events);
	}

	protected RootAggregate() {

		this.concurrencyVersion = 0;
		this.uncommittedEvents = new ArrayList<>();
	}

	public RootAggregateId id() {

		return id;
	}

	public long concurrencyVersion() {

		return concurrencyVersion;
	}

	private void incrementConcurrencyVersion() {

		this.concurrencyVersion++;
	}

	public List<Event> getUncommittedEvents() {

		return Collections.unmodifiableList(uncommittedEvents);
	}

	/**
	 * Should be invoked right after save action
	 */
	public void clearUncommittedEvents() {

		uncommittedEvents.clear();
	}

	public void apply(List<Event> events) {

		Contract.notNull(events, "events");
		events.stream()
			.peek(event -> {
					if (id != null) {
						boolean eventIdMatchRootAggregateId = (id.equals(event.rootAggregateId()));
						Contract.isTrue(eventIdMatchRootAggregateId, format("Event rootAggregateId=%s not match RootAggregate rootAggregateId=%s", id, event.rootAggregateId()));
					}
				}
			)
			.forEach(this::mutateState);
	}

	protected void apply(Event event) {

		//mutate Aggregate for incoming business events
		mutateState(event);
		//put aside not committed events
		uncommittedEvents.add(event);
	}

	protected void mutateState(Event event) {

		Class<? extends RootAggregate> rootAggregateClass = this.getClass();
		Class<? extends Event> eventClass = event.getClass();
		String key = rootAggregateClass.getName() + ":" + eventClass.getName();

		try {
			ofNullable(mutatorMethods.get(key))
				.orElseGet(() -> this.cacheMutatorMethodFor(key, rootAggregateClass, eventClass))
				.invoke(this, event);

			incrementConcurrencyVersion();
		}
		catch (Exception e) {
			throw new RuntimeException(
				format(
					"Method %s(%s) failed. See cause: %s",
					MUTATOR_METHOD_NAME,
					eventClass.getSimpleName(),
					e.getMessage()),
				e);
		}
	}

	private Method cacheMutatorMethodFor(
		String key,
		Class<? extends RootAggregate> rootAggregateClass,
		Class<? extends Event> eventClass) {

		synchronized (mutatorMethods) {
			try {
				Method method = this.hiddenOrPublicMethod(rootAggregateClass, eventClass);
				method.setAccessible(true);
				mutatorMethods.put(key, method);
				return method;
			}
			catch (Exception e) {
				throw new IllegalArgumentException(
					"I do not understand "
						+ MUTATOR_METHOD_NAME
						+ "("
						+ eventClass.getSimpleName()
						+ ") because: "
						+ e.getClass().getSimpleName() + ">>>" + e.getMessage(),
					e);
			}
		}
	}

	private Method hiddenOrPublicMethod(
		Class<? extends RootAggregate> aRootType,
		Class<? extends Event> anEventType)
		throws Exception {

		try {
			// assume protected or private...
			return aRootType.getDeclaredMethod(MUTATOR_METHOD_NAME, anEventType);
		}
		catch (Exception e) {
			// then public...
			return aRootType.getMethod(MUTATOR_METHOD_NAME, anEventType);
		}
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof RootAggregate))
			return false;

		RootAggregate that = (RootAggregate) o;

		return new EqualsBuilder()
			.append(concurrencyVersion, that.concurrencyVersion)
			.append(id, that.id)
			.append(uncommittedEvents, that.uncommittedEvents)
			.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37)
			.append(id)
			.append(concurrencyVersion)
			.append(uncommittedEvents)
			.toHashCode();
	}
}
