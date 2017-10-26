package pl.biltec.yaess.core.common;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class Timer {

	private static final Logger logger = LoggerFactory.getLogger(Timer.class);

	public static <T> T count(String comment, Supplier<T> supplier) {

		Instant start = Instant.now();
		T t = supplier.get();
		logger.info(comment + "=" +  String.valueOf(Duration.between(start, Instant.now())));
		return t;
	}

}
