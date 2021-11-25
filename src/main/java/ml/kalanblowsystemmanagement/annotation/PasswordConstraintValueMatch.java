package ml.kalanblowsystemmanagement.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import ml.kalanblowsystemmanagement.constraint.PasswordConstraintValidator;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 8, max = 12)
@NotEmpty
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
public @interface PasswordConstraintValueMatch {

	String message() default "Invalid password:le password doit contenir:chiffre + caractère minuscule + caractère majuscule + ponctuation + symbole";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String field();
	String fieldMatch();

	@Target({ ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@interface List {
		PasswordConstraintValueMatch[] value();
	}
}
