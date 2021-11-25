
package ml.kalanblowsystemmanagement.config;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.UrlPathHelper;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import ml.kalanblowsystemmanagement.utils.date.FrenchLocalDateFormater;
import ml.kalanblowsystemmanagement.utils.date.LocalDateTimeFormatter;

@Configuration
public class PageConfiguration implements WebMvcConfigurer {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	@Override
	public void addViewControllers(ViewControllerRegistry controllerRegistry) {

		controllerRegistry.addViewController("/").setViewName("login");
		controllerRegistry.addViewController("/home").setViewName("home");
		controllerRegistry.addViewController("/dashboard").setViewName("dashboard");
		controllerRegistry.addViewController("/login").setViewName("login");
		controllerRegistry.addViewController("/adminPage/signup").setViewName("signup");
		controllerRegistry.addViewController("/logout").setViewName("logout");
		controllerRegistry.addViewController("/profile").setViewName("profile");
		controllerRegistry.addViewController("/adminPage/roles").setViewName("roles");
		controllerRegistry.addViewController("/adminPage/roles/newRole").setViewName("newRole");
		controllerRegistry.addViewController("/error").setViewName("error");
		controllerRegistry.addViewController("/adminPage/users").setViewName("users");
		controllerRegistry.addViewController("/lastName").setViewName("lastname");
		controllerRegistry.addViewController("/adminHome").setViewName("homepage");
		controllerRegistry.addViewController("/profile/email").setViewName("changeEmail");
		controllerRegistry.addViewController("/password").setViewName("changePassword");
		controllerRegistry.addViewController("/access_denied").setViewName("accessDenied");

	}

	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setPrefix("classpath:/templates/admin/");
		resolver.setSuffix(".html");
		resolver.setCacheable(false);
		resolver.setCharacterEncoding("UTF-8");
		resolver.setCheckExistence(true);
		return resolver;
	}

	/*
	 * @Bean public ViewResolver htmlViewResolver() { ThymeleafViewResolver resolver
	 * = new ThymeleafViewResolver(); resolver.setTemplateEngine(templateEngine());
	 * resolver.setContentType("text/html; charset=UTF-8");
	 * resolver.setCharacterEncoding("UTF-8"); resolver.setViewNames(new String[] {
	 * "*.html" });
	 * 
	 * return resolver; }
	 */

	/*
	 * @Bean public SpringTemplateEngine templateEngine() { SpringTemplateEngine
	 * templateEngine = new SpringTemplateEngine();
	 * templateEngine.setTemplateResolver(templateResolver());
	 * templateEngine.setEnableSpringELCompiler(true); return templateEngine; }
	 */

	@Bean
	public FilterRegistrationBean<HiddenHttpMethodFilter> hiddenHttpMethodFilter() {
		FilterRegistrationBean<HiddenHttpMethodFilter> filterRegBean = new FilterRegistrationBean<HiddenHttpMethodFilter>(
				new HiddenHttpMethodFilter());
		filterRegBean.setUrlPatterns(Arrays.asList("/*"));
		return filterRegBean;
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.getDefault());
		return slr;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	// to make sure that the Spring Security session registry is notified when the
	// session is destroyed.
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
		// registry.addInterceptor(cookieslocaleResolver());
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	}

	@Override
	public void addFormatters(FormatterRegistry formatterRegistry) {

		formatterRegistry.addFormatterForFieldType(LocalDate.class, new FrenchLocalDateFormater());
		formatterRegistry.addFormatter(new LocalDateTimeFormatter());

	}

	public void configurePathMatch(PathMatchConfigurer configurer) {
		UrlPathHelper urlPathHelper = new UrlPathHelper();
		urlPathHelper.setRemoveSemicolonContent(false);
		configurer.setUrlPathHelper(urlPathHelper);
	}

}
