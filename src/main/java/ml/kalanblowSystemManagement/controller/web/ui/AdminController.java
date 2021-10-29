
package ml.kalanblowSystemManagement.controller.web.ui;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;
import ml.kalanblowSystemManagement.controller.web.command.AdminSignupCommand;
import ml.kalanblowSystemManagement.dto.model.RoleDto;
import ml.kalanblowSystemManagement.dto.model.UserDto;
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
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    public static final String EDIT_USER_FORM = "admin/editUser";
    public static final String REDIRECT_ADMIN_PAGE_USERS = "redirect:/admin/allUsers";
    @Autowired
    private UserService userService;

    private UserFinder userFinder;

    private UserSearchErrorResponse userSearchErrorResponse;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder passworEncoder;

    private ModelMapper modelMapper;

    @RequestMapping(
            value = "/signup",
            method = RequestMethod.GET)
    public ModelAndView signup() {

        ModelAndView modelAndView = new ModelAndView("signup");
        modelAndView.addObject("adminSignupCommand", new AdminSignupCommand());
        modelAndView.addObject("roles", initializeAuthorities());
        modelAndView.addObject("genders", List.of(Gender.FEMALE, Gender.MALE));
        return modelAndView;
    }

    /**
     * @param adminSignupCommand
     * @param bindingResult
     * @param redirectAttributes
     * @return modelAndView
     */

    @RequestMapping(
            value = "/signup",
            method = RequestMethod.POST)
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
        }
        else {

            registerUserAdmin(adminSignupCommand);
            redirectAttributes.addFlashAttribute("message",
                    "SuccesFully added the new user with admin role");
            modelAndView.addObject("successMessage", redirectAttributes);

            log.debug(" A user registered with the email provided");
        }
        return new ModelAndView("redirect:admin/homepage");
    }

    /**
     * Get all users or search users if searching parameters exist
     * 
     * @param pageable
     * @return
     */
    @RequestMapping(
            value = "/users",
            method = RequestMethod.GET)
    public ModelAndView getUsersList(ModelAndView modelAndView,
            UserSearchParameters userSearchParameters) {

        modelAndView = new ModelAndView("admin/allUsers");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<UserDto> userDto = userService.findUserByEmail(authentication.getName());

        // page size
        int selectedPageSize =
                userSearchParameters.getPageSize().orElse(InitialPagingSizes.INITIAL_PAGE_SIZE);
        // Evaluate page size. If requested parameter is null, return initial
        int selectedPage =
                (userSearchParameters.getPage().orElse(0) < 1) ? InitialPagingSizes.INITIAL_PAGE
                        : (userSearchParameters.getPage().get() - 1);

        PageRequest pageRequest =
                PageRequest.of(selectedPage, selectedPageSize, Sort.by(Direction.ASC, "id"));
        UserSearchResult userSearchResult = new UserSearchResult();

        if (userSearchParameters.getPropertyValue().isEmpty()
                || userSearchParameters.getPropertyValue().get().isEmpty()) {
            userSearchResult.setUserPage(userService.listUserByPage(pageRequest));
        }
        else {

            userSearchResult = userFinder.searchUsersByProperty(pageRequest, userSearchParameters);

            if (userSearchResult.isNumberFormatException()) {

                return userSearchErrorResponse.respondToNumberFormatException(userSearchResult,
                        modelAndView);
            }

            if (userSearchResult.getUserPage().getTotalElements() == 0) {

                modelAndView = userSearchErrorResponse.respondToEmptySearchResult(modelAndView,
                        pageRequest);
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
        modelAndView.addObject("userName", userDto.get());
        modelAndView.addObject("authorithy", userDto.get().getRoles());

        return modelAndView;
    }

    /**
     * @param adminSignupCommand
     * @return userDto
     */
    private UserDto registerUserAdmin(@Valid AdminSignupCommand adminSignupCommand) {
        UserDto userDto = new UserDto().setEmail(adminSignupCommand.getEmail())
                .setFirstName(adminSignupCommand.getFirstName())
                .setLastName(adminSignupCommand.getLastName())
                .setPassword(passworEncoder.encode(adminSignupCommand.getPassword()))
                .setMatchingPassword(
                        passworEncoder.encode(adminSignupCommand.getMatchingPassword()))
                .setMobileNumber(adminSignupCommand.getMobileNumber())
                .setBirthDate(adminSignupCommand.getBirthDate())
                .setGender(adminSignupCommand.getGender())
                .setAdresse(adminSignupCommand.getAdresse())
                .setCreatedBy(adminSignupCommand.getCreatedBy())
                .setLastModifiedDate(LocalDateTime.now()).setCreatedDate(LocalDateTime.now());
        UserDto userDto2 = userService.signup(userDto);
        return userDto2;

    }

    /**
     * @param userDto
     * @param id
     * @return
     */
    @GetMapping("/editeUser/{id}")
    public ModelAndView editingUser(Optional<UserDto> userDto, @RequestParam(
            value = "id",
            required = true) int id) {
        log.info("User/edit-Get: Id to query=" + id);
        userDto = Optional.ofNullable(userService.findUserById(Long.valueOf(id)));
        log.info("User/edit-Get: Id to query=" + id);
        Set<RoleDto> roleDtos = roleService.getAllRoles();
        userDto.get().setRoles(userService.getAssignedRoleSet(userDto.get()));
        ModelAndView modelAndView = new ModelAndView(EDIT_USER_FORM);

        modelAndView.addObject("editeUser", userDto.get());
        modelAndView.addObject("roles", roleDtos);

        return modelAndView;
        
    }

    @PostMapping("/editeUser/{id}")
    public ModelAndView editingUser(@ModelAttribute("oldUser") @Valid UserDto userDto,
            @PathVariable Long id,

            BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        ModelAndView modelAndView = new ModelAndView(REDIRECT_ADMIN_PAGE_USERS);
        Optional<UserDto> uOptional = Optional.ofNullable(userService.findUserById(id));
        Set<RoleDto> roleDtos = roleService.getAllRoles();
        boolean emailAlreadyExists =
                userService.findByEmailAndIdNot(userDto.getEmail(), id) != null;
        boolean validationFailed = emailAlreadyExists || bindingResult.hasErrors();

        if (emailAlreadyExists) {
            bindingResult.rejectValue("email", "UniqueUsername.user.username");
        }
        if (validationFailed) {
            modelAndView.addObject("userDto", userDto);
            modelAndView.addObject("roleList", roleDtos);
            modelAndView.addObject("org.springframework.validation.BindingResult.userDto",
                    bindingResult);

            return modelAndView;
        }
        userService.updateUserProfile(uOptional.get());
        redirectAttributes.addFlashAttribute("userHasBeenUpdated", true);
        return new ModelAndView(REDIRECT_ADMIN_PAGE_USERS);

    }

    /**
     * @param id
     * @return
     */
    // @DeleteMapping(value = "/deleteUser")
    @RequestMapping(
            value = "/deleteUser",
            method = RequestMethod.GET)
    public ModelAndView deleteUser(@RequestParam(
            value = "id",
            required = true) int id,
            @RequestParam(
                    value = "phase",
                    required = false,
                    defaultValue = "unknown") String phase) {

        ModelAndView modelAndView = new ModelAndView("admin/userDelete");
        UserDto userDto = new UserDto();
        userDto = userService.findUserById(Long.valueOf(id));
        log.info("user/delet_Get | id=" + id + "|phase= " + phase + "|" + userDto);

        if (phase.equals("stage")) {
            String message = "user" + userDto + "queued for display.";
            modelAndView.addObject("userId", userDto);
            modelAndView.addObject("message", message);
        }
        if (phase.equals("confirm")) {

            modelAndView = new ModelAndView(REDIRECT_ADMIN_PAGE_USERS);
            userDto = userService.deleteUser(userDto);
            String message = "User" + userDto + " was successfully deleted.";
            modelAndView.addObject("message", message);
        }

        if (phase.equals("cancel")) {
            modelAndView = new ModelAndView(REDIRECT_ADMIN_PAGE_USERS);
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

    @RequestMapping(
            value = "/postSearch",
            method = RequestMethod.POST)
    public String postSearch(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String search = request.getParameter("search");
        if (search.toLowerCase().contains(" ")) {
            redirectAttributes.addFlashAttribute("error", "Try using spring instead!");
            return "redirect:/";
        }
        redirectAttributes.addAttribute("search", search);
        return "redirect:result";
    }

    @InitBinder(
            value = "enum")
    public void enumInitBinder(WebDataBinder binder) {
        binder.registerCustomEditor(UserRole.class, new CaseInsensitiveEnumEditor(UserRole.class));
    }

}
