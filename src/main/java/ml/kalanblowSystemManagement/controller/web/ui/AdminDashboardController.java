package ml.kalanblowSystemManagement.controller.web.ui;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import ml.kalanblowSystemManagement.controller.web.command.PasswordFormCommand;
import ml.kalanblowSystemManagement.controller.web.command.ProfileFormCommand;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.service.UserService;

@Controller
public class AdminDashboardController {

	@Autowired
	private UserService userService;

	@GetMapping(value = "/dashboard")
	public ModelAndView dashboard() {
		ModelAndView modelAndView = new ModelAndView("dashboard");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDto> userDto = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("currentUser", userDto);
		modelAndView.addObject("userName", userDto.get().getLastName() + " " + userDto.get().getFirstName());
		return modelAndView;
	}

	@GetMapping(value = "/profile")
	public ModelAndView getUserProfile() {
		ModelAndView modelAndView = new ModelAndView("profile");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDto> userDto = userService.findUserByEmail(auth.getName());
		ProfileFormCommand profileFormCommand = new ProfileFormCommand().setFirstName(userDto.get().getFirstName())
				.setLastName(userDto.get().getLastName()).setMobileNumber(userDto.get().getMobileNumber());
		PasswordFormCommand passwordFormCommand = new PasswordFormCommand().setEmail(userDto.get().getEmail())
				.setPassword(userDto.get().getPassword());
		modelAndView.addObject("profileForm", profileFormCommand);
		modelAndView.addObject("passwordForm", passwordFormCommand);
		modelAndView.addObject("userName", userDto.get().getFullName());
		return modelAndView;
	}

	@PostMapping(value = "/profile")
	public ModelAndView updateProfile(@Valid @ModelAttribute("profileForm") ProfileFormCommand profileFormCommand,
			BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView("profile");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDto> userDto = userService.findUserByEmail(auth.getName());
		PasswordFormCommand passwordFormCommand = new PasswordFormCommand().setEmail(userDto.get().getEmail())
				.setPassword(userDto.get().getPassword());
		modelAndView.addObject("passwordForm", passwordFormCommand);
		modelAndView.addObject("userName", userDto.get().getFullName());
		if (!bindingResult.hasErrors()) {
			userDto.get().setFirstName(profileFormCommand.getFirstName()).setLastName(profileFormCommand.getLastName())
					.setMobileNumber(profileFormCommand.getMobileNumber());
			userService.updateUserProfile(userDto.get());
			modelAndView.addObject("userName", userDto.get().getFullName());
		}
		return modelAndView;
	}

	@PostMapping(value = "/password")
	public ModelAndView changePassword(@Valid @ModelAttribute("passwordForm") PasswordFormCommand passwordFormCommand,
			BindingResult bindingResult) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDto> userDto = userService.findUserByEmail(auth.getName());
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("profile");
			ProfileFormCommand profileFormCommand = new ProfileFormCommand().setFirstName(userDto.get().getFirstName())
					.setLastName(userDto.get().getLastName()).setMobileNumber(userDto.get().getMobileNumber());
			modelAndView.addObject("profileForm", profileFormCommand);
			modelAndView.addObject("userName", userDto.get().getFullName());
			return modelAndView;
		} else {
			userService.changeUserPassword(userDto.get(), passwordFormCommand.getPassword());
			return new ModelAndView("login");
		}
	}

	@GetMapping(value = { "/logout" })
	public String logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
		return "redirect:login";
	}

	@GetMapping(value = "/home")
	public String home() {
		return "redirect:dashboard";
	}

}
