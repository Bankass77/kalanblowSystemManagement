package ml.kalanblowSystemManagement.config;

import java.time.LocalDate;
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
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import ml.kalanblowSystemManagement.utils.FrenchLocalDateFormater;

@Configuration
public class PageConfiguration implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry controllerRegistry) {

		controllerRegistry.addViewController("/").setViewName("login");
		controllerRegistry.addViewController("/home").setViewName("home");
		controllerRegistry.addViewController("/dashboard").setViewName("dashboard");
		controllerRegistry.addViewController("/login").setViewName("login");
		controllerRegistry.addViewController("/signup").setViewName("signup");
		controllerRegistry.addViewController("/logout").setViewName("logout");
		controllerRegistry.addViewController("/profile").setViewName("profile");
		controllerRegistry.addViewController("/error").setViewName("error");
		controllerRegistry.addViewController("/users").setViewName("users");
		controllerRegistry.addViewController("/editeUser/{id}").setViewName("editUser");
		controllerRegistry.addViewController("/updateUser/{id}").setViewName("updateUser");
		controllerRegistry.addViewController("/deleteUser/{id}").setViewName("deleteUser");
		controllerRegistry.addViewController("/adminHome").setViewName("adminHomePage");
		controllerRegistry.addViewController("/profile/email").setViewName("changeEmail");
		controllerRegistry.addViewController("/password").setViewName("changePassword");
		controllerRegistry.addViewController("/access_denied").setViewName("accessDenied");
		controllerRegistry.addViewController("/kalanblow").setViewName("kalanblow");
		controllerRegistry.addViewController("/admin").setViewName("admin");
		controllerRegistry.addViewController("/teacher").setViewName("teacher");
		controllerRegistry.addViewController("/student").setViewName("student");
		controllerRegistry.addViewController("/parent").setViewName("parent");
	}

	
	  @Bean public FilterRegistrationBean hiddenHttpMethodFilter() {
	  FilterRegistrationBean filterRegBean = new FilterRegistrationBean(new
	  HiddenHttpMethodFilter()); filterRegBean.setUrlPatterns(Arrays.asList("/*"));
	  return filterRegBean; }
	 
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	/*
	 * @Bean public LocalValidatorFactoryBean getValidator() {
	 * LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
	 * bean.setValidationMessageSource(messageSource()); return bean; }
	 */

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
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	}

	@Override
	public void addFormatters(FormatterRegistry formatterRegistry) {

		formatterRegistry.addFormatterForFieldType(LocalDate.class, new FrenchLocalDateFormater());

	}
	

}
