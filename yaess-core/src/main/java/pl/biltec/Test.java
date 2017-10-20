package pl.biltec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Test {

	private static final Logger logger = LoggerFactory.getLogger(Test.class);

	public static void main(String[] args) {

		System.out.println("aa");

		logger.info("info");
		logger.warn("warn");
		logger.trace("trace");
		logger.debug("debug");
		logger.error("error");
	}

}
