package ml.kalanblowSystemManagement.controller.web.ui;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.exception.EntityType;
import ml.kalanblowSystemManagement.exception.ExceptionType;
import ml.kalanblowSystemManagement.exception.KalanblowSystemManagementException;
import ml.kalanblowSystemManagement.model.User;
import ml.kalanblowSystemManagement.service.UserService;

@Controller
@Slf4j
public class LoginController {

	private UserService userService;

	//private final  PersistentTokenBasedRememberMeServices rememberMeServices;

	@Autowired
	public LoginController(UserService userService) {
		super();
		this.userService = userService;
	
	}

	@GetMapping(value = { "/", "/login" })
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("login");
		  if (error != null) {
	            modelAndView.addObject("error", "Invalid username and password!");
	            
	      
	            }

		if (logout != null) {
			modelAndView.addObject("logout", "You've been logged out successfully.");
		}

		return modelAndView;
	}

	@SneakyThrows
	@GetMapping("kalanblow")
	public String tokalanblow(ModelAndView modelAndView, HttpSession session) {
		UserDto userDto = (UserDto) session.getAttribute("userName");
		User user = new ModelMapper().map(userDto, User.class);
		modelAndView.addObject("userName", user);
		if (("ADMIN").equals(user.getRoles())) {
			return "admin/homepage";
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
	public String adminIndex(Authentication authentication, HttpSession session) {
		showUser(authentication, session);
		return "redirect:kalanblow";
	}

	@GetMapping("/student")
	public String studentIndex(Authentication authentication, HttpSession session) {
		showUser(authentication, session);
		return "redirect:/kalanblow";
	}

	@GetMapping("/teacher")
	public String teachearIndex(Authentication authentication, HttpSession session) {
		showUser(authentication, session);
		return "redirect:/kalanblow";
	}

	@GetMapping("/parent")
	public String parentIndex(Authentication authentication, HttpSession session) {
		showUser(authentication, session);
		return "redirect:/kalanblow";
	}

	protected void showUser(Authentication authentication, HttpSession session) {
		Optional<UserDto> userDto = Optional.ofNullable((UserDto) session.getAttribute("userName"));

		if (userDto.isPresent()) {
			userDto = userService.findUserByEmail(authentication.getName());
			log.debug("userDto is:",userDto.get());
			session.setAttribute("userName", userDto);
		}
	}

	private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
		return KalanblowSystemManagementException.throwException(entityType, exceptionType, args);

	}

}
