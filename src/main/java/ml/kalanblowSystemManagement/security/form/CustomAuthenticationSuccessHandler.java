
package ml.kalanblowSystemManagement.security.form;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.io.IOException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ml.kalanblowSystemManagement.exception.EntityType;
import ml.kalanblowSystemManagement.exception.ExceptionType;
import ml.kalanblowSystemManagement.exception.KalanblowSystemManagementException;

@Component

@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@SneakyThrows

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_OK);

		for (GrantedAuthority auth : authentication.getAuthorities()) {
			log.debug(auth.getAuthority());
			if ("ADMIN".equals(auth.getAuthority())) {
				redirectStrategy.sendRedirect(request, response, "/dashboard");
				log.info("admin" + auth.getAuthority() + "is logged in");
				break;
			} else if ("STUDENT".equals(auth.getAuthority())) {

				redirectStrategy.sendRedirect(request, response, "/dashboard");
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
	}

	private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
		return KalanblowSystemManagementException.throwException(entityType, exceptionType, args);

	}
}
