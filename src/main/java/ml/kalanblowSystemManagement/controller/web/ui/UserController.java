
package ml.kalanblowSystemManagement.controller.web.ui;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;
import ml.kalanblowSystemManagement.controller.web.command.AdminSignupCommand;
import ml.kalanblowSystemManagement.dto.model.RoleDto;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.model.EditeMode;
import ml.kalanblowSystemManagement.model.Gender;
import ml.kalanblowSystemManagement.model.UserRole;
import ml.kalanblowSystemManagement.service.RoleService;
import ml.kalanblowSystemManagement.service.UserService;
import ml.kalanblowSystemManagement.service.searching.UserFinder;
import ml.kalanblowSystemManagement.service.searching.UserSearchErrorResponse;
import ml.kalanblowSystemManagement.service.searching.UserSearchParameters;
import ml.kalanblowSystemManagement.service.searching.UserSearchResult;
import ml.kalanblowSystemManagement.utils.CaseInsensitiveEnumEditor;
import ml.kalanblowSystemManagement.utils.date.FrenchLocalDateFormater;
import ml.kalanblowSystemManagement.utils.paging.InitialPagingSizes;
import ml.kalanblowSystemManagement.utils.paging.Pager;

@Controller
@Slf4j
@RequestMapping("/users")
public class UserController {

	public static final String EDIT_USER_FORM = "users/editeUser";

	public static final String REDIRECT_ADMIN_PAGE_USERS = "redirect:/users/allUsers";

	@Autowired
	private UserService userService;

	private UserFinder userFinder;

	private UserSearchErrorResponse userSearchErrorResponse;

	@Autowired
	private RoleService roleService;

	@Autowired
	private BCryptPasswordEncoder passworEncoder;

	private ModelMapper modelMapper;

	@GetMapping("/signup")
	public ModelAndView signup() {

		ModelAndView modelAndView = new ModelAndView("signup");
		modelAndView.addObject("adminSignupCommand", new AdminSignupCommand());
		modelAndView.addObject("roles", initializeAuthorities());
		modelAndView.addObject("genders", List.of(Gender.FEMALE, Gender.MALE));
		modelAndView.addObject("editeMode", EditeMode.CREATE);
		return modelAndView;
	}

	/**
	 * @param adminSignupCommand
	 * @param bindingResult
	 * @param redirectAttributes
	 * @return modelAndView
	 */

	@PostMapping(value = "/signup")
	@Secured("ADMIN")
	public ModelAndView creatnewUserAdmin(
			@Valid @ModelAttribute("adminSignupCommand") AdminSignupCommand adminSignupCommand,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView("signup");

		if (!userService.emailExist(adminSignupCommand.getEmail())) {

			bindingResult.reject("There is already a user registered with the email provided");
			log.debug("There is already a user registered with the email provided");
		}

		if (bindingResult.hasErrors()) {

			return modelAndView;
		} else {

			registerUserAdmin(adminSignupCommand);
			redirectAttributes.addFlashAttribute("message", "SuccesFully added the new user with admin role");
			modelAndView.addObject("successMessage", redirectAttributes);
			modelAndView.addObject("editeMode", EditeMode.CREATE);
			modelAndView.addObject("roles", initializeAuthorities());

			log.debug(" A user registered with the email provided");
		}
		return new ModelAndView("redirect:/admin/homepage");
	}

	/**
	 * Get all users or search users if searching parameters exist
	 * 
	 * @param pageable
	 * @return
	 */
	@GetMapping("/list")
	@PreAuthorize("hasRole('ADMIN')and hasRole('TEACHER') and hasRole('STAFF')")
	@PostFilter("hasPermission(filterObject, 'read') or hasPermission(filterObject, 'ADMIN')")
	public ModelAndView getUsersList(ModelAndView modelAndView, UserSearchParameters userSearchParameters) {

		modelAndView = new ModelAndView("users/allUsers");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Optional<UserDto> userDto = userService.findUserByEmail(authentication.getName());

		// page size
		if (userSearchParameters.getPage().isPresent()) {
			Optional<Integer>  selectedPageSize = Optional.ofNullable(userSearchParameters.getPageSize().orElse(InitialPagingSizes.INITIAL_PAGE_SIZE));
			// Evaluate page size. If requested parameter is null, return initial
			Optional<Integer> selectedPage = Optional.ofNullable((userSearchParameters.getPage().orElse(0) < 1) ? InitialPagingSizes.INITIAL_PAGE
					: (userSearchParameters.getPage().get() - 1));
			PageRequest pageRequest = PageRequest.of(Integer.valueOf(selectedPage.get()),Integer.valueOf( selectedPageSize.get()), Sort.by(Direction.ASC, "id"));
			UserSearchResult userSearchResult = new UserSearchResult();

			if (userSearchParameters.getPropertyValue().isEmpty()
					|| userSearchParameters.getPropertyValue().get().isEmpty()) {
				userSearchResult.setUserPage(userService.listUserByPage(pageRequest));
			} else {

				userSearchResult = userFinder.searchUsersByProperty(pageRequest, userSearchParameters);

				if (userSearchResult.isNumberFormatException()) {

					return userSearchErrorResponse.respondToNumberFormatException(userSearchResult, modelAndView);
				}

				if (userSearchResult.getUserPage().getTotalElements() == 0) {

					modelAndView = userSearchErrorResponse.respondToEmptySearchResult(modelAndView, pageRequest);
					userSearchResult.setUserPage(userService.findAllPageable(pageRequest));
				}

				modelAndView.addObject("usersProperty", userSearchParameters.getUsersProperty().get());
				modelAndView.addObject("propertyValue", userSearchParameters.getPropertyValue().get());
			}

			Pager pager = new Pager(userSearchResult.getUserPage().getTotalPages(),
					userSearchResult.getUserPage().getNumber(), InitialPagingSizes.BUTTONS_TO_SHOW,
					userSearchResult.getUserPage().getTotalElements());
			modelAndView.addObject("pager", pager);
			modelAndView.addObject("users", userSearchResult.getUserPage());
			modelAndView.addObject("selectedPageSize", selectedPageSize);
			modelAndView.addObject("pageSizes", InitialPagingSizes.PAGE_SIZES);
		}


		if (userDto.isPresent()) {
			modelAndView.addObject("userName", userDto.get());
			modelAndView.addObject("authorithy", userDto.get().getRoles());
		}


		return modelAndView;
	}

	/**
	 * @param adminSignupCommand
	 * @return userDto
	 */
	private UserDto registerUserAdmin(@Valid AdminSignupCommand adminSignupCommand) {
		UserDto userDto = new UserDto().setEmail(adminSignupCommand.getEmail())
				.setFirstName(adminSignupCommand.getFirstName()).setLastName(adminSignupCommand.getLastName())
				.setPassword(passworEncoder.encode(adminSignupCommand.getPassword()))
				.setMatchingPassword(passworEncoder.encode(adminSignupCommand.getMatchingPassword()))
				.setMobileNumber(adminSignupCommand.getMobileNumber()).setBirthDate(adminSignupCommand.getBirthDate())
				.setGender(adminSignupCommand.getGender()).setAdresse(adminSignupCommand.getAdresse())
				.setCreatedBy(adminSignupCommand.getCreatedBy()).setLastModifiedDate(LocalDateTime.now())
				.setCreatedDate(LocalDateTime.now());

		return userService.signup(userDto);

	}

	/**
	 * @param userDto
	 * @param id
	 * @return
	 */
	@GetMapping("/editeUser/{id}")
	public String getEditingUser(@PathVariable Long id, Model model) {
		log.info("Admin/edit-Get: Id to query=" + id);
		Optional<UserDto> userDto = Optional.ofNullable(userService.findUserById(id));
		log.info("Admin/edit-Get: Id to query=" + id);
		Set<RoleDto> roleDtos = roleService.getAllRoles();
		userDto.get().setRoles(userService.getAssignedRoleSet(userDto.get()));

		model.addAttribute("editeUser", userDto.get());
		model.addAttribute("roles", roleDtos);
		model.addAttribute("editMode", EditeMode.UPDATE);

		return EDIT_USER_FORM;

	}

	@PostMapping("/editeUser/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	//@preAuthorize("@userAuthorization.can('update', 'ADMIN') or @userAuthorization.can('update', 'ADMIN', #userId)")
	public String updateUser(@ModelAttribute("oldUser") @Valid UserDto userDto, @PathVariable (value = "userId") Long id, Model model,

			BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		Optional<UserDto> uOptional = Optional.ofNullable(userService.findUserById(id));
		Set<RoleDto> roleDtos = roleService.getAllRoles();
		boolean emailAlreadyExists = userService.findByEmailAndIdNot(userDto.getEmail(), id) != null;
		boolean validationFailed = emailAlreadyExists || bindingResult.hasErrors();

		if (emailAlreadyExists) {
			bindingResult.rejectValue("email", "UniqueUsername.user.username");
		}
		if (validationFailed) {
			model.addAttribute("editeUser", userDto);
			model.addAttribute("roleList", roleDtos);
			model.addAttribute("org.springframework.validation.BindingResult.userDto", bindingResult);
			model.addAttribute("editeMode", EditeMode.UPDATE);

			return EDIT_USER_FORM;
		}
		userService.updateUserProfile(uOptional.get());
		redirectAttributes.addFlashAttribute("userHasBeenUpdated", true);
		return REDIRECT_ADMIN_PAGE_USERS;

	}

	/**
	 * @param id
	 * @return
	 */
	@PostMapping("/deleteUser/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ModelAndView deleteUser(@PathVariable("id") Long id) {

		ModelAndView modelAndView = new ModelAndView("users/userDelete");
		UserDto userDto = new UserDto();
		userDto = userService.findUserById(Long.valueOf(id));

		modelAndView = new ModelAndView(REDIRECT_ADMIN_PAGE_USERS);
		userDto = userService.deleteUser(userDto);
		String message = "Admin" + userDto + " was successfully deleted.";
		modelAndView.addObject("message", message);

		return modelAndView;

	}

	@ModelAttribute("roles")
	public Set<RoleDto> initializeAuthorities() {

		Set<RoleDto> roleDtos = roleService.getAllRoles();
		return roleDtos;
	}

	@ModelAttribute("dateFormat")
	public String localeFormat(Locale locale) {
		return FrenchLocalDateFormater.getPattern(locale);

	}

	@RequestMapping(value = "/postSearch", method = RequestMethod.POST)
	public String postSearch(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String search = request.getParameter("search");
		if (search.toLowerCase().contains(" ")) {
			redirectAttributes.addFlashAttribute("error", "Try using spring instead!");
			return "redirect:/";
		}
		redirectAttributes.addAttribute("search", search);
		return "redirect:result";
	}

	@InitBinder(value = "enum")
	public void enumInitBinder(WebDataBinder binder) {
		binder.registerCustomEditor(UserRole.class, new CaseInsensitiveEnumEditor(UserRole.class));
	}

}
