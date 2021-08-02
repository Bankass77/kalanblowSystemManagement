package ml.kalanblowSystemManagement.controller.web.ui;

import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.SneakyThrows;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.exception.EntityType;
import ml.kalanblowSystemManagement.exception.ExceptionType;
import ml.kalanblowSystemManagement.exception.KalanblowSystemManagementException;
import ml.kalanblowSystemManagement.model.User;
import ml.kalanblowSystemManagement.service.UserService;

@Controller
public class LoginController {

	private UserService userService;

	@Autowired
	public LoginController(UserService userService) {
		super();
		this.userService = userService;
	}

	@GetMapping(value = { "/", "/login" })
	public ModelAndView login() {
		return new ModelAndView("login");
	}

	@SneakyThrows

	@GetMapping("kalanblow")
	public String tokalanblow(ModelAndView modelAndView, HttpSession session) {
		UserDto userDto = (UserDto) session.getAttribute("user");
		User user = new ModelMapper().map(userDto, User.class);
		modelAndView.addObject("user", user);
		if (("ADMIN").equals(user.getRoles())) {
			return "admin/admin";
		} else if ("STUDENT".equals(user.getRoles())) {
			return "student/student";
		} else if ("TEACHER".equals(user.getRoles())) {
			return "teacher/teacher";
		} else if ("PARENT".equals(user.getRoles())) {
			return "parent/parent";
		} else {
			throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, user.getEmail());
		}
	}

	@GetMapping("/admin")
	public String adminIndex(Principal principal, HttpSession session) {
		showUser(principal, session);
		return "redirect:kalanblow";
	}

	@GetMapping("/student")
	public String engineerIndex(Principal principal, HttpSession session) {
		showUser(principal, session);
		return "redirect:/kalanblow";
	}

	@GetMapping("/teacher")
	public String directorIndex(Principal principal, HttpSession session) {
		showUser(principal, session);
		return "redirect:/kalanblow";
	}

	@GetMapping("/parent")
	public String workerIndex(Principal principal, HttpSession session) {
		showUser(principal, session);
		return "redirect:/kalanblow";
	}

	protected void showUser(Principal principal, HttpSession session) {
		UserDto userDto =  (UserDto) session.getAttribute("user");

		if (userDto !=null) {
			userDto = userService.findUserByEmail(principal.getName());
			session.setAttribute("user", userDto);
		}
	}

	private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
		return KalanblowSystemManagementException.throwException(entityType, exceptionType, args);

	}

}
