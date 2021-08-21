package ml.kalanblowSystemManagement.constraint;



import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


@Email(message="Please provide a valid email address")
@Documented
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@NotEmpty
@Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",message="Invalid Email:le fomat du mail est incorrect")
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
public @interface EmailContraint {
	String message() default "Invalid Email:le fomat du mail est incorrect";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
