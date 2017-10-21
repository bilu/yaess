package pl.biltec.yaess.core.common.exception;

import java.util.regex.Pattern;

public class PatternNotMatchedException extends ContractBrokenException {

	private static final long serialVersionUID = 1L;

	public PatternNotMatchedException(Pattern pattern, String value, String patternDescription) {
		super(formatMessage(pattern, value, patternDescription));
	}

	private static String formatMessage(Pattern pattern, String value, String patternDescription) {
		return String.format("String [%s] doesn't match pattern %s [%s]", value, patternDescription, pattern);
	}
}
