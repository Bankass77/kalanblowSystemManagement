
package ml.kalanblowSystemManagement.config;

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
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.UrlPathHelper;
import ml.kalanblowSystemManagement.utils.date.FrenchLocalDateFormater;
import ml.kalanblowSystemManagement.utils.date.LocalDateTimeFormatter;


@Configuration
public class PageConfiguration implements WebMvcConfigurer {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	@Override
	public void addViewControllers(ViewControllerRegistry controllerRegistry) {

		controllerRegistry.addViewController("/").setViewName("login");
		controllerRegistry.addViewController("/login").setViewName("login");
		controllerRegistry.addViewController("/home").setViewName("home");
		controllerRegistry.addViewController("/dashboard").setViewName("dashboard");
		controllerRegistry.addViewController("/login").setViewName("login");
		controllerRegistry.addViewController("/users/signup").setViewName("signup");
		controllerRegistry.addViewController("/logout").setViewName("logout");
		controllerRegistry.addViewController("/admin/profile").setViewName("admin/profile");
		controllerRegistry.addViewController("/role/roles").setViewName("roles");
		controllerRegistry.addViewController("/role/newRole").setViewName("newRole");
		controllerRegistry.addViewController("/error/403").setViewName("403");
		controllerRegistry.addViewController("/error/409").setViewName("409");
		controllerRegistry.addViewController("/error/404").setViewName("404");
		controllerRegistry.addViewController("/error/5xx").setViewName("5xx");
		controllerRegistry.addViewController("/users/allUsers").setViewName("users/allUsers");
		controllerRegistry.addViewController("/user/search").setViewName("user/search");
		controllerRegistry.addViewController("/lastName").setViewName("lastname");
		controllerRegistry.addViewController("/admin/homepage").setViewName("admin/homepage");
		controllerRegistry.addViewController("/access_denied").setViewName("accessDenied");

	}

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
		slr.setDefaultLocale(new Locale("fr"));
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
	/** Multipart file uploading configuratioin */
	/*
	 * @Bean public CommonsMultipartResolver multipartResolver() throws IOException
	 * { CommonsMultipartResolver resolver = new CommonsMultipartResolver();
	 * resolver.setMaxUploadSize(10000000); return resolver; }
	 */
	/**
	 * Spring Boot allows configuring Content Negotiation using properties
	 */
	/*
	 * @Override public void configureContentNegotiation(final
	 * ContentNegotiationConfigurer configurer) {
	 * configurer.favorPathExtension(true) .favorParameter(true)
	 * .parameterName("mediaType") .ignoreAcceptHeader(false)
	 * .useRegisteredExtensionsOnly(false)
	 * .defaultContentType(MediaType.APPLICATION_JSON) .mediaType("xml",
	 * MediaType.APPLICATION_XML) .mediaType("json", MediaType.APPLICATION_JSON); }
	 */

}
