
package ml.kalanblowSystemManagement.controller.web.ui;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.service.UserService;

@Controller
public class DashboardController {

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

	@GetMapping(value = "/home")
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("redirect:/dashboard");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDto> userDto = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("currentUser", "Welcome " + userDto.get().getFullName());
		return modelAndView;
	}

	@GetMapping("/staffHomePage")
	@Secured("STAFF")
	public ModelAndView adminPage() {
		ModelAndView modelAndView = new ModelAndView("staff/homepage");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDto> userDOptional = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("currentUser", auth.getName());
		modelAndView.addObject("Authority", userDOptional.get().getRoles());
		modelAndView.addObject("adminMessage", "Ce contenu est disponible uniquement pour l'utilisateur user!");

		return modelAndView;
	}

	@PreAuthorize("hasRole('STUDENT')")
	@GetMapping(value = "/studentPage")
	public ModelAndView userPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDto> uOptional = userService.findUserByEmail(authentication.getName());

		ModelAndView retVal = new ModelAndView();
		retVal.addObject("currentUser", authentication.getName());
		retVal.addObject("Authority", uOptional.get().getRoles());

		return retVal;
	}

}
