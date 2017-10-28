package pl.biltec.yaess.core.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Justification for access level relaxation
 */
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD })
@Retention(RetentionPolicy.SOURCE)
public @interface MustExist {

	/**
	 * Access level relaxation reason
	 */
	Reason[] reason() default Reason.I_DONT_KNOW_HOW_TO_USE_IT;

	/**
	 * @return Access level relaxation out of Reason scope or more detailed reason
	 */
	String extendedReason() default "";


	enum Reason {

		TESTING_PURPOSE,
		/**
		 * ex. JSON(de)serialization, convertion tools
		 */
		MAPPING,
		/**
		 * ex. boilerplate limitation
		 */
		REFLECTION,
		/**
		 * described in different way
		 */
		OTHER,

		I_DONT_KNOW_HOW_TO_USE_IT;
	}
}