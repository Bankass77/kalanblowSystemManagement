
package ml.kalanblowSystemManagement.controller.web.ui;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import ml.kalanblowsystemmanagement.controller.web.command.PasswordFormCommand;
import ml.kalanblowsystemmanagement.controller.web.command.ProfileFormCommand;
import ml.kalanblowsystemmanagement.dto.model.UserDto;
import ml.kalanblowsystemmanagement.service.UserService;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;

    @GetMapping(
            value = "/dashboard")
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView("dashboard");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserDto> userDto = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("currentUser", userDto);
        modelAndView.addObject("userName",
                userDto.get().getLastName() + " " + userDto.get().getFirstName());
        return modelAndView;
    }

    @GetMapping(
            value = "/profile")
    public ModelAndView getUserProfile() {
        ModelAndView modelAndView = new ModelAndView("profile");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserDto> userDto = userService.findUserByEmail(auth.getName());
        ProfileFormCommand profileFormCommand = new ProfileFormCommand()
                .setFirstName(userDto.get().getFirstName()).setLastName(userDto.get().getLastName())
                .setMobileNumber(userDto.get().getMobileNumber());
        PasswordFormCommand passwordFormCommand = new PasswordFormCommand()
                .setEmail(userDto.get().getEmail()).setPassword(userDto.get().getPassword());
        modelAndView.addObject("profileForm", profileFormCommand);
        modelAndView.addObject("passwordForm", passwordFormCommand);
        modelAndView.addObject("userName", userDto.get().getFullName());
        return modelAndView;
    }

    @PostMapping(
            value = "/profile")
    public ModelAndView updateProfile(
            @Valid @ModelAttribute("profileForm") ProfileFormCommand profileFormCommand,
            BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("profile");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserDto> userDto = userService.findUserByEmail(auth.getName());
        PasswordFormCommand passwordFormCommand = new PasswordFormCommand()
                .setEmail(userDto.get().getEmail()).setPassword(userDto.get().getPassword());
        modelAndView.addObject("passwordForm", passwordFormCommand);
        modelAndView.addObject("userName", userDto.get().getFullName());
        if (!bindingResult.hasErrors()) {
            userDto.get().setFirstName(profileFormCommand.getFirstName())
                    .setLastName(profileFormCommand.getLastName())
                    .setMobileNumber(profileFormCommand.getMobileNumber());
            userService.updateUserProfile(userDto.get());
            modelAndView.addObject("userName", userDto.get().getFullName());
        }
        return modelAndView;
    }

    @PostMapping("/profile/email")
    public ModelAndView saveUserEmail(String email, BindingResult bindingResult) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<UserDto> userDto = userService.findUserByEmail(authentication.getName());

        if (!bindingResult.hasErrors()) {

            ModelAndView modelAndView = new ModelAndView("profile");
            ProfileFormCommand profileFormCommand =
                    new ProfileFormCommand().setFirstName(userDto.get().getFirstName())
                            .setLastName(userDto.get().getLastName())
                            .setMobileNumber(userDto.get().getMobileNumber());
            modelAndView.addObject("profileForm", profileFormCommand);
            modelAndView.addObject("userName", userDto.get().getFullName());
        }
        else {
            userService.changeUserEmail(email);

            return new ModelAndView("login");
        }

        ModelAndView modelAndView = new ModelAndView("profile");
        modelAndView.addObject("newEmail", userDto);
        return new ModelAndView("admin/user/allUsers");
    }

    @PostMapping(
            value = "/password")
    public ModelAndView changePassword(
            @Valid @ModelAttribute("passwordForm") PasswordFormCommand passwordFormCommand,
            BindingResult bindingResult) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserDto> userDto = userService.findUserByEmail(auth.getName());
        if (!bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("profile");
            ProfileFormCommand profileFormCommand =
                    new ProfileFormCommand().setFirstName(userDto.get().getFirstName())
                            .setLastName(userDto.get().getLastName())
                            .setMobileNumber(userDto.get().getMobileNumber());
            modelAndView.addObject("profileForm", profileFormCommand);
            modelAndView.addObject("userName", userDto.get().getFullName());
            return modelAndView;
        }
        else {
            userService.changeUserPassword(userDto.get().getPassword(),
                    passwordFormCommand.getPassword());
            return new ModelAndView("login");
        }
    }

    @GetMapping(
            value = {
                "/logout"
            })
    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:login";
    }

    @GetMapping(
            value = "/home")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("redirect:dashboard");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserDto> userDto = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + userDto.get().getFullName());
        return modelAndView;
    }

    
    /**
     * login successfull jump
     * @return
     */
    @GetMapping("/adminHome")
    public ModelAndView adminHomePage() {
        ModelAndView modelAndView = new ModelAndView("admin/homepage");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserDto> userDOptional = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("currentUser", auth.getPrincipal());
        modelAndView.addObject("Authority", userDOptional.get().getRoles());
        modelAndView.addObject("adminMessage",
                "Ce contenu est disponible uniquement pour l'utilisateur l'admin!");

        return modelAndView;
    }

    @GetMapping("/staffHomePage")
    @PreAuthorize("hasRole('STAFF')")
    public ModelAndView adminPage() {
        ModelAndView modelAndView = new ModelAndView("admin/homepage");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserDto> userDOptional = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("currentUser", auth.getName());
        modelAndView.addObject("Authority", userDOptional.get().getRoles());
        modelAndView.addObject("adminMessage",
                "Ce contenu est disponible uniquement pour l'utilisateur user!");

        return modelAndView;
    }

    
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping(value="/studentPage")
    public ModelAndView userPage()
    {
         ModelAndView retVal = new ModelAndView();
         retVal.setViewName("studentPage");
         return retVal;
    }
    
    @GetMapping(
            value = {
                "/access_denied"
            })
    public ModelAndView acccessDenied() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/403");
        return modelAndView;
    }

    @GetMapping("/search")
    public String search() {
        return "search";
    }

}
