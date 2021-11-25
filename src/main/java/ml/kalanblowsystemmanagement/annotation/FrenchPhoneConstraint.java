package ml.kalanblowsystemmanagement.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Documented
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@NotEmpty
@Pattern(regexp = "^(?:(?:\\+|00)33[\\s.-]{0,3}(?:\\(0\\)[\\s.-]{0,3})?|0)[1-9](?:(?:[\\s.-]?\\d{2}){4}|\\d{2}(?:[\\s.-]?\\d{3}){2})$", message = "Invalid Phone:le fomat du téléphone est incorrect")
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
public @interface FrenchPhoneConstraint {
	String message() default "Invalid Phone:le fomat du numéro de téléphone est incorrect e";

	/**
	 * 
	 * 0123456789 01 23 45 67 89 1.23.45.67.89 0123 45.67.89 0033 123-456-789
	 * +33-1.23.45.67.89 +33 - 123 456 789 +33(0) 123 456 789 +33 (0)123 45 67 89
	 * +33 (0)1 2345-6789 +33(0) - 123456789
	 */
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
