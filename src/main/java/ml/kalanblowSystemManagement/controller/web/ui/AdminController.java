package ml.kalanblowSystemManagement.controller.web.ui;

import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;
import ml.kalanblowSystemManagement.controller.web.command.AdminSignupCommand;
import ml.kalanblowSystemManagement.dto.model.RoleDto;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.service.RoleService;
import ml.kalanblowSystemManagement.service.UserService;

@Controller
@Slf4j
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@GetMapping(value = "/signup")
	public ModelAndView signup() {

		ModelAndView modelAndView = new ModelAndView("signup");
		modelAndView.addObject("adminSignupCommand", new AdminSignupCommand());
		return modelAndView;
	}

	/**
	 * @param adminSignupCommand
	 * @param bindingResult
	 * @param redirectAttributes
	 * @return modelAndView
	 */

	@PostMapping(value = "/signup")
	public ModelAndView creatnewUserAdmin(
			@Valid @ModelAttribute("adminSignupCommand") AdminSignupCommand adminSignupCommand,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		Optional<UserDto> userDto = userService.findUserByEmail(adminSignupCommand.getEmail());
		ModelAndView modelAndView = new ModelAndView("signup");

		if (userDto.isPresent()) {

			bindingResult.reject("There is already a user registered with the email provided");
			log.debug("There is already a user registered with the email provided");
		}

		if (bindingResult.hasErrors()) {

			return modelAndView;
		} else {

			registerUserAdmin(adminSignupCommand);
			redirectAttributes.addFlashAttribute("message", "SuccesFully added the new user with admin role");
			modelAndView.addObject("successMessage", redirectAttributes);
			log.debug(" A user registered with the email provided");
		}
		return modelAndView;
	}

	/**
	 * @param pageable
	 * @return
	 */
	@GetMapping(value = "/users")
	public ModelAndView getUsersList(Pageable pageable) {

		ModelAndView modelAndView = new ModelAndView("admin/allUsers");

		Set<UserDto> userDtos = userService.getAllUsers();

		Page<UserDto> pageInfo = userService.listUserByPage(pageable);

		modelAndView.addObject("users", userDtos);
		modelAndView.addObject("page", pageInfo);
		return modelAndView;
	}

	/**
	 * @param adminSignupCommand
	 * @return userDto
	 */
	private UserDto registerUserAdmin(@Valid AdminSignupCommand adminSignupCommand) {
		UserDto userDto = new UserDto().setEmail(adminSignupCommand.getEmail())
				.setFirstName(adminSignupCommand.getFirstName()).setLastName(adminSignupCommand.getLastName())
				.setPassword(adminSignupCommand.getPassword()).setAdmin(true);
		UserDto userDto2 = userService.signup(userDto);
		return userDto2;

	}

	/**
	 * @param userDto
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/editeUser/{id}")
	public ModelAndView getUserById(Optional<UserDto> userDto, int id) {

		userDto = userService.findUserById((long) id);

		ModelAndView modelAndView = new ModelAndView("admin/editUser");

		modelAndView.addObject("editUser", userDto);

		return modelAndView;

	}

	@PostMapping("/updateUser/{id}")
	public ModelAndView updateUser(@PathVariable("id") int id, @ModelAttribute Optional<UserDto> userDto) {

		ModelAndView modelAndView = new ModelAndView("admin/allUsers");
		userDto = userService.findUserById((long) id);

		if (userDto.isPresent()) {
			userDto = Optional.ofNullable(userService.updateUserProfile(userDto.get()));
			modelAndView.addObject("updateUser", userDto);
		}
		return new ModelAndView("redirect:/users");

	}

	/**
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/deleteUser/{id}")
	public ModelAndView deleteUser(@PathVariable("id") int id) {

		ModelAndView modelAndView = new ModelAndView("admin/editUser");

		UserDto userDto = userService.deleteUserById((long) id);

		modelAndView.addObject("userId", userDto);

		return modelAndView;

	}

	@ModelAttribute("roles")
	public Set<RoleDto> initializeAuthorities() {

		Set<RoleDto> roleDtos = roleService.getAllRoles();
		return roleDtos;
	}
}
