
package ml.kalanblowSystemManagement.controller.web.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class LoginController {

	@GetMapping(value = { "/", "/login" })
	public ModelAndView login(@AuthenticationPrincipal UserDetails userDetails) {
		ModelAndView modelAndView = new ModelAndView("login");
		if (userDetails == null) {

			log.debug("in login:{}", userDetails);
			return modelAndView;

		} else {
			return new ModelAndView("redirect:/dashboard");
		}

	}

	@PostMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null) {

			new SecurityContextLogoutHandler().logout(request, response, authentication);

		}
		return "redirect:login";
	}

	@GetMapping(value = { "/access_denied" })
	public ModelAndView acccessDenied() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error/403");
		return modelAndView;
	}

}
