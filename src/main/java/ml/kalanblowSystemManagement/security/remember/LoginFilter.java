package ml.kalanblowSystemManagement.security.remember;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import ml.kalanblowSystemManagement.controller.web.request.LoginRequest;

/**
 * Processes login requests and delegates deciding the decision to {@link AuthenticationManager}.
 */
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

	  public static final String REMEMBER_ME_ATTRIBUTE = "remember-me";

	  private final ObjectMapper objectMapper;

	  /**
	   * Create a login filter.
	   * @param filterProcessesUrl filter's URL
	   * @param objectMapper a ObjectMapper instance
	   */
	  public LoginFilter(String filterProcessesUrl, ObjectMapper objectMapper) {
	    super(new AntPathRequestMatcher(filterProcessesUrl, "POST"));
	    this.objectMapper = objectMapper;
	  }

	  public Authentication attemptAuthentication(HttpServletRequest request,
	      HttpServletResponse response) throws AuthenticationException {

	    if (!request.getMethod().equals("POST")) {
	      throw new AuthenticationServiceException(
	          "Authentication method not supported: " + request.getMethod());
	    }

	    try {
	      LoginRequest loginRequest =
	          objectMapper.readValue(request.getInputStream(), LoginRequest.class);

	      String username = Optional
	          .ofNullable(loginRequest.getEmail())
	          .map(String::trim)
	          .orElse("");

	      String password = Optional
	          .ofNullable(loginRequest.getPassword())
	          .orElse("");

	      request.setAttribute(REMEMBER_ME_ATTRIBUTE, loginRequest.getRememberMe());

	      UsernamePasswordAuthenticationToken authRequest =
	          new UsernamePasswordAuthenticationToken(username, password);

	      return this.getAuthenticationManager().authenticate(authRequest);
	    } catch (AuthenticationException ae) {
	      throw ae;
	    } catch (Exception e) {
	      throw new InternalAuthenticationServiceException(e.getMessage(), e);
	    }
	  }
}
