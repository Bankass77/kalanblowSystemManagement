package ml.kalanblowSystemManagement.controller.web.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;
import ml.kalanblowSystemManagement.controller.web.command.PasswordFormCommand;
import ml.kalanblowSystemManagement.service.UserService;

@Controller
@RequestMapping("/password")
@Slf4j
public class PasswordResetController {

	private static final String REDIRECT_LOGIN = "redirect:/login";
	private static final String MSG = "resetPasswordMsg";

	private final UserService userService;

	private final MessageSource messageSource;

	@Autowired
	public PasswordResetController(UserService userService, MessageSource messageSource) {
		super();
		this.userService = userService;
		this.messageSource = messageSource;
	}

	@PostMapping("/request")
	public String resetPassword(final PasswordFormCommand passwordFormCommand, RedirectAttributes redirAttr) {

		try {

			userService.forgottenPassword(passwordFormCommand.getEmail());
		} catch (Exception e) {
			log.debug(e.getMessage());
		}

		redirAttr.addFlashAttribute(MSG,
				messageSource.getMessage("user.forgotpwd.msg", null, LocaleContextHolder.getLocale()));
		return REDIRECT_LOGIN;
	}

	@GetMapping("/change")
	public String changePassword(@RequestParam(required = false) String token, final RedirectAttributes redirAttr,
			final Model model) {
		if (StringUtils.isEmpty(token)) {
			redirAttr.addFlashAttribute("tokenError", messageSource
					.getMessage("user.registration.verification.missing.token", null, LocaleContextHolder.getLocale()));
			return REDIRECT_LOGIN;
		}

		PasswordFormCommand data = new PasswordFormCommand();
		data.setToken(token);
		setResetPasswordForm(model, data);

		return "/admin/changePassword";
	}

	private void setResetPasswordForm(final Model model, PasswordFormCommand data) {
		model.addAttribute("forgotPassword", data);
	}

}
