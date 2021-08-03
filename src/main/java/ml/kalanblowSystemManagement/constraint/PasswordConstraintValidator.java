package ml.kalanblowSystemManagement.constraint;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import ml.kalanblowSystemManagement.dto.model.UserDto;

public class PasswordConstraintValidator implements ConstraintValidator<PasswordConstraint, Object> {

	@Override
	public void initialize(PasswordConstraint arg0) {
	}

	@Override
	public boolean isValid(Object password, ConstraintValidatorContext context) {

		UserDto userDto = (UserDto) password;
		PasswordValidator validator = new PasswordValidator(Arrays.asList(
				// at least 8 characters
				new LengthRule(8, 100),

				// at least one upper-case character
				new CharacterRule(EnglishCharacterData.UpperCase, 1),

				// at least one lower-case character
				new CharacterRule(EnglishCharacterData.LowerCase, 1),

				// at least one digit character
				new CharacterRule(EnglishCharacterData.Digit, 1),

				// at least one symbol (special character)
				new CharacterRule(EnglishCharacterData.Special, 1),

				// no whitespace
				new WhitespaceRule()

		));
		RuleResult result = validator.validate(new PasswordData(userDto.getPassword()));
		if (result.isValid()) {
			return true;
		}
		List<String> messages = validator.getMessages(result);

		String messageTemplate = messages.stream().collect(Collectors.joining(","));
		context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation()
				.disableDefaultConstraintViolation();
		return userDto.getPassword().equals(userDto.getMatchingPassword());
	}
}
