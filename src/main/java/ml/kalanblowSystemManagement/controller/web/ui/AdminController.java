package ml.kalanblowSystemManagement.controller.web.ui;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ml.kalanblowSystemManagement.controller.web.command.PasswordFormCommand;
import ml.kalanblowSystemManagement.controller.web.command.ProfileFormCommand;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private final UserService userService;

	@Autowired
	public AdminController(UserService service) {
		super();
		this.userService = service;
	}

	@Value("${spring.application.name}")
	String appName;

	/**
	 * login successfull jump
	 * 
	 * @return
	 */
	@GetMapping("/homepage")
	@PreAuthorize("hasRole('ADMIN')")
	public ModelAndView adminHomePage() {
		ModelAndView modelAndView = new ModelAndView("admin/homepage");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDto> userDOptional = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("currentUser", auth.getPrincipal());
		modelAndView.addObject("Authority", userDOptional.get().getRoles());
		modelAndView.addObject("adminMessage", "Ce contenu est disponible uniquement pour l'utilisateur l'admin!");
		modelAndView.addObject("appName", appName);

		return modelAndView;
	}

	@GetMapping(value = "/profile")
	public ModelAndView getUserProfile() {
		ModelAndView modelAndView = new ModelAndView("admin/profile");
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
	@PreAuthorize("hasRole('ADMIN')")
	public ModelAndView updateProfile(@Valid @ModelAttribute("profileForm") ProfileFormCommand profileFormCommand,
			BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView("admin/profile");
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

	@PostMapping("/profile/email")
	@PreAuthorize("hasRole('ADMIN')")
	public ModelAndView saveUserEmail(String email, BindingResult bindingResult) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Optional<UserDto> userDto = userService.findUserByEmail(authentication.getName());

		if (!bindingResult.hasErrors()) {

			ModelAndView modelAndView = new ModelAndView("admin/profile");
			ProfileFormCommand profileFormCommand = new ProfileFormCommand().setFirstName(userDto.get().getFirstName())
					.setLastName(userDto.get().getLastName()).setMobileNumber(userDto.get().getMobileNumber());
			modelAndView.addObject("profileForm", profileFormCommand);
			modelAndView.addObject("userName", userDto.get().getFullName());
		} else {
			userService.changeUserEmail(email);

			return new ModelAndView("login");
		}

		ModelAndView modelAndView = new ModelAndView("admin/profile");
		modelAndView.addObject("newEmail", userDto);
		return new ModelAndView("users/allUsers");
	}

	@PostMapping(value = "/password")
	@PreAuthorize("hasRole('ADMIN')")
	public ModelAndView changePassword(@Valid @ModelAttribute("passwordForm") PasswordFormCommand passwordFormCommand,
			BindingResult bindingResult) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDto> userDto = userService.findUserByEmail(auth.getName());
		if (!bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("admin/profile");
			ProfileFormCommand profileFormCommand = new ProfileFormCommand().setFirstName(userDto.get().getFirstName())
					.setLastName(userDto.get().getLastName()).setMobileNumber(userDto.get().getMobileNumber());
			modelAndView.addObject("profileForm", profileFormCommand);
			modelAndView.addObject("userName", userDto.get().getFullName());
			return modelAndView;
		} else {
			userService.changeUserPassword(userDto.get().getPassword(), passwordFormCommand.getPassword());
			return new ModelAndView("login");
		}
	}

}
