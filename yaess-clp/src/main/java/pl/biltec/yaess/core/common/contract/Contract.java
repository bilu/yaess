package pl.biltec.yaess.core.common.contract;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import pl.biltec.yaess.core.common.contract.exception.CollectionContainsNullValueException;
import pl.biltec.yaess.core.common.contract.exception.CollectionIsNullOrEmptyException;
import pl.biltec.yaess.core.common.contract.exception.ConditionNotMetException;
import pl.biltec.yaess.core.common.contract.exception.ContractBrokenException;
import pl.biltec.yaess.core.common.contract.exception.IncorrectValueException;
import pl.biltec.yaess.core.common.contract.exception.ReferenceIsNullException;
import pl.biltec.yaess.core.common.contract.exception.TextIsNullOrEmptyException;
import pl.biltec.yaess.core.common.contract.exception.PatternNotMatchedException;


public final class Contract {

	private Contract() {

	}

	public static <T extends Object> T notNull(T object, String objectName) {

		notNull(object, () -> new ReferenceIsNullException(objectName));
		return object;
	}

	public static String notNullOrEmpty(String object, String objectName) {

		notNullOrEmpty(object, () -> new TextIsNullOrEmptyException(objectName));
		return object;
	}

	public static boolean isTrue(boolean condition, String message) {

		isTrue(condition, () -> new ConditionNotMetException(message));
		return condition;
	}

	public static boolean isFalse(boolean condition, String message) {

		isFalse(condition, () -> new ConditionNotMetException(message));
		return condition;
	}

	public static <E> Collection<E> containsNoNulls(Collection<E> collection, String objectName) {

		containsNoNulls(collection, () -> new CollectionContainsNullValueException(objectName));
		return collection;
	}

	public static <K, V> Map<K, V> notNullOrEmpty(Map<K, V> map, String objectName) {

		notNullOrEmpty(map, () -> new CollectionIsNullOrEmptyException(objectName));
		return map;
	}

	public static <E> Collection<E> notNullOrEmpty(Collection<E> collection, String objectName) {

		notNullOrEmpty(collection, () -> new CollectionIsNullOrEmptyException(objectName));
		return collection;
	}

	public static String hasLengthBetween(String value, int minInclusive, int maxInclusive, String objectName) {

		isTrue(value.length() >= minInclusive
				&& value.length() <= maxInclusive,
			() -> {
				final String message = String.format("%s has length %d, should be between %d and %d inclusive", objectName, value.length(), minInclusive, maxInclusive);
				return new ConditionNotMetException(message);
			});
		return value;
	}

	public static String matches(String value, Pattern pattern, String patternName) {

		isTrue(pattern.matcher(value).matches(), () -> new PatternNotMatchedException(pattern, value, patternName));
		return value;
	}

	public static <T extends ContractBrokenException, E> Collection<E> containsNoNulls(Collection<E> collection, Supplier<T> exceptionSupplier) {

		notNull(collection, exceptionSupplier);
		isTrue(collection.stream().allMatch(Objects::nonNull), exceptionSupplier);
		return collection;
	}

	public static <T extends ContractBrokenException> boolean isTrue(boolean condition, Supplier<T> exceptionSupplier) {

		if (!condition) {
			throw exceptionSupplier.get();
		}
		return condition;
	}

	public static <T extends ContractBrokenException> boolean isFalse(boolean condition, Supplier<T> exceptionSupplier) {

		if (condition) {
			throw exceptionSupplier.get();
		}
		return condition;
	}

	public static <T extends ContractBrokenException, O> O notNull(O notNullObject, Supplier<T> exceptionSupplier) {

		if (notNullObject == null) {
			throw exceptionSupplier.get();
		}
		return notNullObject;
	}

	public static <T extends ContractBrokenException> String notNullOrEmpty(String notNullOrEmpty, Supplier<T> exceptionSupplier) {

		if (isEmpty(notNullOrEmpty)) {
			throw exceptionSupplier.get();
		}
		return notNullOrEmpty;
	}

	private static boolean isEmpty(String notNullOrEmpty) {

		return notNullOrEmpty == null || notNullOrEmpty.isEmpty();

	}

	public static <T extends ContractBrokenException, E> Collection<E> notNullOrEmpty(Collection<E> collection, Supplier<T> exceptionSupplier) {

		if (isEmpty(collection)) {
			throw exceptionSupplier.get();
		}
		return collection;
	}

	private static <E> boolean isEmpty(Collection<E> collection) {

		return collection == null || collection.isEmpty();
	}

	public static <T extends ContractBrokenException> boolean exists(File file, Supplier<T> exceptionSupplier) {

		if (file.exists()) {
			return true;
		}
		throw exceptionSupplier.get();
	}

	public static <T extends ContractBrokenException, K, V> Map<K, V> notNullOrEmpty(Map<K, V> map, Supplier<T> exceptionSupplier) {

		if (isEmpty(map)) {
			throw exceptionSupplier.get();
		}
		return map;
	}

	private static <K, V> boolean isEmpty(Map<K, V> map) {

		return map == null || map.isEmpty();
	}

	public static <T> T inAllowedValues(T objectValue, String objectName, Collection<T> allowedObjectValues) {

		notNull(objectValue, "objectValue");
		notNullOrEmpty(objectName, "objectName");
		notNull(allowedObjectValues, "allowedObjectValues");

		if (!allowedObjectValues.contains(objectValue)) {
			throw new IncorrectValueException(objectName, objectValue, allowedObjectValues);
		}
		return objectValue;
	}
}
