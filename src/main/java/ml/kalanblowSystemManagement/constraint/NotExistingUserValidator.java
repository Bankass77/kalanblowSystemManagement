
package ml.kalanblowSystemManagement.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import ml.kalanblowSystemManagement.controller.web.command.AdminSignupCommand;
import ml.kalanblowSystemManagement.exception.NotExistingUser;
import ml.kalanblowSystemManagement.service.UserService;

public class NotExistingUserValidator
        implements ConstraintValidator<NotExistingUser, AdminSignupCommand> {

    private final UserService userService;

    @Autowired
    public NotExistingUserValidator(UserService userService) {
        super();
        this.userService = userService;
    }

    @Override
    public boolean isValid(AdminSignupCommand value, ConstraintValidatorContext context) {
        if (userService.emailExist(value.getEmail())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{UserAlreadyExisting}")
                    .addPropertyNode("email").addConstraintViolation();
            return false;
        }
        return true;
    }

}
