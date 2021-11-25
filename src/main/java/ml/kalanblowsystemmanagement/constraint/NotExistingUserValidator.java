
package ml.kalanblowsystemmanagement.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import ml.kalanblowsystemmanagement.controller.web.command.AdminSignupCommand;
import ml.kalanblowsystemmanagement.exception.NotExistingUser;
import ml.kalanblowsystemmanagement.service.UserService;

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
