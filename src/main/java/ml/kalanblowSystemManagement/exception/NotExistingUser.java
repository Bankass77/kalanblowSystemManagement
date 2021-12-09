
package ml.kalanblowSystemManagement.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import ml.kalanblowSystemManagement.constraint.NotExistingUserValidator;

@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME)
@Constraint(  validatedBy = NotExistingUserValidator.class) 
public @interface NotExistingUser {

    String message() default "{UserAlreadyExisting}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
