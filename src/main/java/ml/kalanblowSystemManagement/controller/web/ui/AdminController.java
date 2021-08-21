package ml.kalanblowSystemManagement.controller.web.ui;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;
import ml.kalanblowSystemManagement.controller.web.command.AdminSignupCommand;
import ml.kalanblowSystemManagement.dto.model.RoleDto;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.model.User;
import ml.kalanblowSystemManagement.model.UserRole;
import ml.kalanblowSystemManagement.service.RoleService;
import ml.kalanblowSystemManagement.service.UserService;
import ml.kalanblowSystemManagement.utils.CaseInsensitiveEnumEditor;
import ml.kalanblowSystemManagement.utils.FrenchLocalDateFormater;
import ml.kalanblowSystemManagement.utils.Pager;
import ml.kalanblowSystemManagement.utils.InitialPagingSizes;

@Controller
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private BCryptPasswordEncoder passworEncoder;

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public ModelAndView signup() {

		ModelAndView modelAndView = new ModelAndView("signup");
		modelAndView.addObject("adminSignupCommand", new AdminSignupCommand());
		modelAndView.addObject("roles", initializeAuthorities());
		return modelAndView;
	}

	/**
	 * @param adminSignupCommand
	 * @param bindingResult
	 * @param redirectAttributes
	 * @return modelAndView
	 */

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ModelAndView creatnewUserAdmin(
			@Valid @ModelAttribute("adminSignupCommand") AdminSignupCommand adminSignupCommand,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		// UserDto userDto = userService.findUserByEmail(adminSignupCommand.getEmail());
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

			log.debug(" A user registered with the email provided");
		}
		return new ModelAndView("redirect:admin/homepage");
	}

	/**
	 * @param pageable
	 * @return
	 */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ModelAndView getUsersList(Pageable pageable, @RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page) {

		ModelAndView modelAndView = new ModelAndView("admin/allUsers");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Optional<UserDto> userDto = userService.findUserByEmail(authentication.getName());

		Page<UserDto> pageInfo = userService.findAllPageableOrderByLastName(userDto.get().toString(), pageable);

		final Page<User> uPage = pageInfo.map(new Function<UserDto, User>() {

			@Override
			public User apply(UserDto userDto) {

				User user = new User().setId(userDto.getId()).setBirthDate(userDto.getBirthDate())
						.setEmail(userDto.getEmail()).setFirstName(userDto.getFirstName())
						.setLastName(userDto.getLastName())
						.setMatchingPassword(passworEncoder.encode(userDto.getMatchingPassword()))
						.setMobileNumber(userDto.getMobileNumber())
						.setPassword(passworEncoder.encode(userDto.getPassword())).setGender(userDto.getGender())
						.setRoles(userDto.getRoleDtos().stream()
								.map(role -> new ml.kalanblowSystemManagement.model.Role()).collect(Collectors.toSet()))
						.setCreatedDate(userDto.getCreatedDate()).setLastModifiedDate(userDto.getLastModifiedDate())
						.setAdresse(userDto.getAdresse()).setAdressePro(userDto.getAdressePro());
				return user;
			}
		});
		// Evaluate page size. If requested parameter is null, return initial
		// page size
		int evalPageSize = pageSize.orElse(InitialPagingSizes.INITIAL_PAGE_SIZE);
		// Evaluate page. If requested parameter is null or less than 0 (to
		// prevent exception), return initial size. Otherwise, return value of
		// param. decreased by 1.
		int evalPage = (page.orElse(0) < 1) ? InitialPagingSizes.INITIAL_PAGE_SIZE : page.get() - 1;

		Page<UserDto> userPage = userService.listUserByPage(PageRequest.of(evalPage, evalPageSize));

		int totalPages = userPage.getTotalPages();

		Pager pager = new Pager(totalPages, uPage.getNumber(), InitialPagingSizes.BUTTONS_TO_SHOW, totalPages);

		if (totalPages > 0) {

			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());

			modelAndView.addObject("pageNumbers", pageNumbers);
		}
		modelAndView.addObject("users", uPage);
		// evaluate page size
		modelAndView.addObject("selectedPageSize", evalPageSize);
		// add page sizes
		modelAndView.addObject("pageSizes", InitialPagingSizes.PAGE_SIZES);
		// add pager
		modelAndView.addObject("pager", pager);
		modelAndView.addObject("userName", userDto.get());
		modelAndView.addObject("authorithy", userDto.get().getRoleDtos());
		return modelAndView;
	}

	@RequestMapping(value = "/users/{lastName}", method = RequestMethod.GET)
	public Set<UserDto> showUserListwithLastName(ModelAndView modelAndView, @RequestParam("lastName") String lastName) {
		if (lastName != null) {

			Set<UserDto> userDtos = userService.findUserByLastName(lastName);

			modelAndView.addObject("users", userDtos);
			return userDtos;
		}

		else
			return userService.getAllUsers();
	}

	/**
	 * @param adminSignupCommand
	 * @return userDto
	 */
	private UserDto registerUserAdmin(@Valid AdminSignupCommand adminSignupCommand) {
		UserDto userDto = new UserDto().setEmail(adminSignupCommand.getEmail())
				.setFirstName(adminSignupCommand.getFirstName()).setLastName(adminSignupCommand.getLastName())
				.setPassword(passworEncoder.encode(adminSignupCommand.getPassword())).setAdmin(true)
				.setMatchingPassword(passworEncoder.encode(adminSignupCommand.getMatchingPassword()))
				.setMobileNumber(adminSignupCommand.getMobileNumber()).setBirthDate(adminSignupCommand.getBirthDate())
				.setGender(adminSignupCommand.getGender()).setAdressePro(adminSignupCommand.getAdressePro())
				.setAdresse(adminSignupCommand.getAdresse());
		UserDto userDto2 = userService.signup(userDto);
		return userDto2;

	}

	/**
	 * @param userDto
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/editeUser", method = RequestMethod.GET)
	public ModelAndView editingUserByIdPage(Optional<UserDto> userDto,
			@RequestParam(value = "id", required = true) int id) {
		log.info("User/edit-Get: Id to query=" + id);
		userDto = Optional.ofNullable(userService.findUserById(Long.valueOf(id)));
		log.info("User/edit-Get: Id to query=" + id);

		ModelAndView modelAndView = new ModelAndView("admin/editeUser");

		modelAndView.addObject("editeUser", userDto.get());
		modelAndView.addObject("roles", initializeAuthorities());

		return modelAndView;

	}

	@RequestMapping(value = "/editeUser", method = RequestMethod.POST)
	public ModelAndView editingUserByIdPages(@ModelAttribute UserDto userDto,
			@RequestParam(value = "action", required = true) String action, BindingResult bindingResult) {

		ModelAndView modelAndView = new ModelAndView("redirect:/admin/allUsers");
		String message = null;

		if (action.equals("save")) {
			userService.editUser(userDto);
			message = "Strategy was successfully edited.";
			log.info("User/edit-Post: Id to query=" + userDto);
			modelAndView.addObject("message", message);
			return modelAndView;
		}

		if (action.equals("Cancel")) {
			message = "user" + userDto + "edit cancelled";
		}
		if (bindingResult.hasErrors()) {

			return modelAndView;
		}
		return modelAndView;

	}

	/**
	 * @param id
	 * @return
	 */
	// @DeleteMapping(value = "/deleteUser")
	@RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
	public ModelAndView deleteUser(@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "phase", required = false, defaultValue = "unknown") String phase) {

		ModelAndView modelAndView = new ModelAndView("admin/userDelete");
		UserDto userDto = new UserDto();
		userDto = userService.findUserById(Long.valueOf(id));
		log.info("user/delet_Get | id=" + id + "|phase= " + phase + "|" + userDto);

		if (phase.equals("stage")) {
			String message = "user" + userDto.getId() + "queued for display.";
			modelAndView.addObject("userId", userDto);
			modelAndView.addObject("message", message);
		}
		if (phase.equals("confirm")) {

			modelAndView = new ModelAndView("redirect:/admin/allUsers");
			userDto = userService.deleteUserById(userDto.getId());
			String message = "User" + userDto.getId() + " was successfully deleted.";
			modelAndView.addObject("message", message);
		}

		if (phase.equals("cancel")) {
			modelAndView = new ModelAndView("redirect:/admin/allUsers");
			String message = "User delete was cancelled.";
			modelAndView.addObject("message", message);
		}

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
