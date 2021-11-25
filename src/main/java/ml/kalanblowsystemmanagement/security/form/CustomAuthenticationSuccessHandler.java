
package ml.kalanblowsystemmanagement.security.form;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.io.IOException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ml.kalanblowsystemmanagement.config.PropertiesConfig;
import ml.kalanblowsystemmanagement.exception.EntityType;
import ml.kalanblowsystemmanagement.exception.ExceptionType;
import ml.kalanblowsystemmanagement.exception.KalanblowSystemManagementException;
import ml.kalanblowsystemmanagement.model.User;
import ml.kalanblowsystemmanagement.service.impl.DeviceService;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Autowired
	private DeviceService deviceService;

	@Autowired
	PropertiesConfig propertiesConfig;

	private static final int ONE_DAY_MINUTES = 24 * 60;
	private static final String X_SET_AUTHORIZATION_BEARER_HEADER = "X-Set-Authorization-Bearer";
	
	
	@SneakyThrows
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_OK);

		for (GrantedAuthority auth : authentication.getAuthorities()) {
			log.debug(auth.getAuthority());
			if ("ADMIN".equals(auth.getAuthority())) {
				redirectStrategy.sendRedirect(request, response, "/adminHome");
				log.info("admin" + auth.getAuthority() + "is logged in");
				break;
			}
			else if ("STAFF".equals(auth.getAuthority())) {

				redirectStrategy.sendRedirect(request, response, "/staffHomePage");
				log.info("Staff" + auth.getAuthority() + "is logged in");
				break;

			} 
			else if ("STUDENT".equals(auth.getAuthority())) {

				redirectStrategy.sendRedirect(request, response, "/home");
				log.info("student" + auth.getAuthority() + "is logged in");
				break;

			} else if ("TEACHER".equals(auth.getAuthority())) {
				redirectStrategy.sendRedirect(request, response, "/dashboard");
				log.info("teacher" + auth.getAuthority() + "is logged in");
				break;
			} else if ("PARENT".equals(auth.getAuthority())) {
				redirectStrategy.sendRedirect(request, response, "/dashboard");
				log.info("parent" + auth.getAuthority() + "is logged in");
				break;
			} else {
				log.warn("Role unknow!");

				throw exception(EntityType.ROLE, ExceptionType.ENTITY_NOT_FOUND, "Role unknow");
			}
		}

	
		clearAuthenticatrionAttributes(request);
		loginNotification(authentication, request);
	}

	private void loginNotification(Authentication authentication, HttpServletRequest req) {

		try {
			if (authentication.getPrincipal() instanceof User && isGeoIpLibEnabled()) {
				deviceService.verifyDevice((User) authentication.getPrincipal(), req);
			}
		} catch (Exception e) {
			log.error("An error occured while verify device or location", e);

			throw exception(EntityType.DEVICE, ExceptionType.ENTITY_EXCEPTION, e.getMessage());
		}
	}

	private boolean isGeoIpLibEnabled() {

		return Boolean.parseBoolean(propertiesConfig.getConfigValue("geao.ip.lib.enabled").toString());
	}

	protected void clearAuthenticatrionAttributes(final HttpServletRequest req) {

		final HttpSession session = req.getSession(false);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

	private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
		return KalanblowSystemManagementException.throwException(entityType, exceptionType, args);

	}
}
