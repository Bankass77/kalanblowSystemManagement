package ml.kalanblowSystemManagement.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

import ml.kalanblowSystemManagement.annotation.PasswordConstraintValueMatch;

public class PasswordFieldsValueMatchValidator implements ConstraintValidator<PasswordConstraintValueMatch, Object> {

	private String field;

	private String fieldMatch;
	private String message;

	public void initialize(PasswordConstraintValueMatch constraint) {
		this.field = constraint.field();
		this.fieldMatch = constraint.fieldMatch();
		this.message = constraint.message();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {

		Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);

		Object fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);

		boolean isValid = false;

		if (fieldMatchValue != null) {
			isValid = fieldValue.equals(fieldMatchValue);
		}

		if (!isValid) {

			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(message).addPropertyNode(field).addConstraintViolation();

			context.buildConstraintViolationWithTemplate(message).addPropertyNode(fieldMatch).addConstraintViolation();
		}
		return isValid;
	}

}
