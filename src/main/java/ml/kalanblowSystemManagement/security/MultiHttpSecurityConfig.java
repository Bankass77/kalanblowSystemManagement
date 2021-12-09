
package ml.kalanblowSystemManagement.security;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import ml.kalanblowSystemManagement.model.UserRole;
import ml.kalanblowSystemManagement.security.api.ApiJWTAuthenticationFilter;
import ml.kalanblowSystemManagement.security.api.ApiJWTAuthorizationFilter;
import ml.kalanblowSystemManagement.security.form.CustomAuthenticationSuccessHandler;
import ml.kalanblowSystemManagement.security.form.CustomLogoutSuccessHandler;
import ml.kalanblowSystemManagement.security.remember.JpaPesristentTokenRepository;

@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, mode = AdviceMode.PROXY, proxyTargetClass = true)
@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MultiHttpSecurityConfig {

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

		private final BCryptPasswordEncoder passwordEncoder;

		private final KalanblowSystemManagementCustomService customService;

		@Autowired
		public ApiWebSecurityConfigurationAdapter(BCryptPasswordEncoder passwordEncoder,
				@Lazy KalanblowSystemManagementCustomService customService) {
			super();
			this.passwordEncoder = passwordEncoder;
			this.customService = customService;
		}

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
			http.httpBasic();

		}

	}

	@Configuration
	@Order(2)
	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		private final BCryptPasswordEncoder passwordEncoder;

		private final KalanblowSystemManagementCustomService customService;

		private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

		private final JpaPesristentTokenRepository jpersistentTokenRepository;

		@Autowired
		public FormLoginWebSecurityConfigurerAdapter(BCryptPasswordEncoder passwordEncoder,
				@Lazy KalanblowSystemManagementCustomService customService,
				CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
				JpaPesristentTokenRepository jpersistentTokenRepository

		) {
			super();
			this.passwordEncoder = passwordEncoder;
			this.customService = customService;
			this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
			this.jpersistentTokenRepository = jpersistentTokenRepository;
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {

			auth.eraseCredentials(true).userDetailsService(customService).passwordEncoder(passwordEncoder);
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().antMatchers("/").permitAll().antMatchers("/images/*").permitAll()
					.antMatchers("/login").permitAll().antMatchers("/users/editeUser/{id}")
					.access("@webSecurity.checkUserId(authentication,#userId)").antMatchers("/users/**")
					.hasAnyRole(UserRole.ADMIN.getUserRole(), UserRole.STAFF.getUserRole()).antMatchers("/user/**")
					.permitAll().antMatchers("/admin/**")
					.hasAnyRole(UserRole.ADMIN.getUserRole(), UserRole.STAFF.getUserRole()).antMatchers("/student/**")
					.hasAnyRole(UserRole.ADMIN.getUserRole(), UserRole.STAFF.getUserRole(),
							UserRole.STUDENT.getUserRole())
					.anyRequest().authenticated().and().cors().and().csrf().disable().formLogin().loginPage("/login")
					.permitAll().failureUrl("/login?error=true").usernameParameter("email")
					.passwordParameter("password").successHandler(customAuthenticationSuccessHandler).and().logout()
					.permitAll().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
					.logoutSuccessHandler(new CustomLogoutSuccessHandler()).deleteCookies("JSESSIONID")
					.logoutSuccessUrl("/").and().exceptionHandling();

			http.rememberMe().key("remember-me").tokenRepository(jpersistentTokenRepository)
					.userDetailsService(customService).tokenValiditySeconds((int) SecurityConstants.EXPIRATION_TIME);

		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**",
					"/resources/static/**", "/css/**", "/js/**", "/img/**", "/fonts/**", "/images/**", "/scss/**",
					"/vendor/**", "/favicon.ico", "/auth/**", "/favicon.png", "/v2/api-docs", "/configuration/ui",
					"/configuration/security", "/webjars/**", "/swagger-resources/**", "/actuator", "/swagger-ui/**",
					"/actuator/**", "/swagger-ui/index.html", "/swagger-ui/");
		}

		@Bean(name = "GeoIPCountry")
		public DatabaseReader databaseReader() throws IOException, GeoIp2Exception {
			final File resource = new File("src/main/resources/maxmind/GeoLite2-Country.mmdb");
			return new DatabaseReader.Builder(resource).build();
		}

		@Bean
		public RoleHierarchy roleHierarchy() {
			RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
			String hierarchy = UserRole.getRoleHiearchy();
			roleHierarchy.setHierarchy(hierarchy);
			return roleHierarchy;
		}

		public SecurityExpressionHandler<FilterInvocation> webSecurityExpressionHandler() {
			DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
			expressionHandler.setRoleHierarchy(roleHierarchy());
			return expressionHandler;
		}

		@Bean
		public Java8TimeDialect thymelaea() {

			return new Java8TimeDialect();
		}

	}

}