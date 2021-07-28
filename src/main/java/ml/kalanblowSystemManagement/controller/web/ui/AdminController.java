package ml.kalanblowSystemManagement.controller.web.ui;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;
import ml.kalanblowSystemManagement.controller.web.command.AdminSignupCommand;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.service.UserService;

@Controller
@Slf4j
public class AdminController {

	@Autowired
	private UserService userService;

	public ModelAndView creatnewUser(@Valid @ModelAttribute("adminSignupCommand") AdminSignupCommand adminSignupCommand,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		Optional<UserDto> userDto = userService.findUserByEmail(adminSignupCommand.getEmail());

		if (userDto.isPresent()) {

			bindingResult.reject("There is already a user registered with the email provided");
				log.debug("There is already a user registered with the email provided");
		}

		ModelAndView modelAndView = new ModelAndView("sign/signup");

		if (bindingResult.hasErrors()) {

			return modelAndView;
		} else {

			registerUserAdmin(adminSignupCommand);
			redirectAttributes.addFlashAttribute("message", "SuccesFully added the new user with admin role");
			modelAndView.addObject("successMessage", redirectAttributes);
			log.debug(" A user registered with the email provided");
		}
		return null;
	}

	private UserDto registerUserAdmin(@Valid AdminSignupCommand adminSignupCommand) {
		   UserDto userDto= new UserDto().setEmail(adminSignupCommand.getEmail())
				   .setFirstName(adminSignupCommand.getFirstName()).setLastName(adminSignupCommand.getLastName())
				   .setPassword(adminSignupCommand.getPassword()).setAdmin(true);
		   UserDto userDto2= userService.signup(userDto);
		return userDto2;

	}

}
