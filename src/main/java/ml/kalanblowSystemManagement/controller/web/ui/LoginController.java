
package ml.kalanblowSystemManagement.controller.web.ui;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
			return new ModelAndView("redirect:/");
		}

	}

}
