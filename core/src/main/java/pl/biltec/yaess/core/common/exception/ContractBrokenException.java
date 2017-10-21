package pl.biltec.yaess.core.common.exception;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Wspólny wyjątek, rzucany przez operacje warstwy aplikacyjnej i domenowej.
 * Zalecenia do nazwy:
 * <ul>
 *     <li>wyraża czynność</li>
 *     <li>ma rzeczownik i czasownik</li>
 * </ul>
 * 
 */
public abstract class ContractBrokenException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ContractBrokenException(String message) {
		super(message);
	}

	public ContractBrokenException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ContractBrokenException)) {
			return false;
		}

		ContractBrokenException operationException = (ContractBrokenException) obj;
		return new EqualsBuilder()
			.append(this.getClass(), operationException.getClass())
			.append(this.getMessage(), operationException.getMessage())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.getClass()).append(this.getMessage()).toHashCode();
	}
}