
package ml.kalanblowSystemManagement.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import ml.kalanblowSystemManagement.security.api.ApiJWTAuthenticationFilter;
import ml.kalanblowSystemManagement.security.api.ApiJWTAuthorizationFilter;
import ml.kalanblowSystemManagement.security.form.CustomAuthenticationSuccessHandler;
import ml.kalanblowSystemManagement.security.form.CustomLogoutSuccessHandler;

@EnableWebSecurity
public class MultiHttpSecurityConfig {

	@Configuration

	@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

		@Autowired
		private BCryptPasswordEncoder passwordEncoder;

		@Autowired
		private KalanblowSystemManagementCustomService customService;

		@Override
		protected void configure(AuthenticationManagerBuilder authenticationManager) throws Exception {

			authenticationManager.userDetailsService(customService).passwordEncoder(passwordEncoder);
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable().antMatcher("/api/**").authorizeRequests().antMatchers("/api/v1/user/**").permitAll()
					.anyRequest().authenticated().and().exceptionHandling()
					.authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
					.addFilter(new ApiJWTAuthenticationFilter(authenticationManager()))
					.addFilter(new ApiJWTAuthorizationFilter(authenticationManager())).sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		}
	}

	@Configuration

	@Order(2)
	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Autowired
		private BCryptPasswordEncoder passwordEncoder;

		@Autowired
		private KalanblowSystemManagementCustomService customService;

		@Autowired
		private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {

			auth.userDetailsService(customService).passwordEncoder(passwordEncoder);
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.cors().and().csrf().disable().authorizeRequests().antMatchers("/").permitAll().antMatchers("/login")
					.permitAll().antMatchers("/signup").permitAll().antMatchers("/dashboard/**").hasAuthority("ADMIN")
					.anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll()
					.failureUrl("/login?error=true").usernameParameter("email").passwordParameter("password")
					.successHandler(customAuthenticationSuccessHandler).and().logout().permitAll()
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
					.logoutSuccessHandler(new CustomLogoutSuccessHandler()).deleteCookies("JSESSIONID")
					.logoutSuccessUrl("/").and().exceptionHandling();
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**",
					"/resources/static/**", "/css/**", "/js/**", "/img/**", "/fonts/**", "/images/**", "/scss/**",
					"/vendor/**", "/favicon.ico", "/auth/**", "/favicon.png", "/v2/api-docs", "/configuration/ui",
					"/configuration/security", "/webjars/**", "/swagger-resources/**", "/actuator", "/swagger-ui/**",
					"/actuator/**", "/swagger-ui/index.html", "/swagger-ui/");
		}

	}

}
