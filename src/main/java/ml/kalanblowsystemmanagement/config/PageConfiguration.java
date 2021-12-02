
package ml.kalanblowsystemmanagement.config;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.UrlPathHelper;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import ml.kalanblowsystemmanagement.utils.date.FrenchLocalDateFormater;
import ml.kalanblowsystemmanagement.utils.date.LocalDateTimeFormatter;
import nz.net.ultraq.thymeleaf.LayoutDialect;

@Configuration
public class PageConfiguration implements  WebMvcConfigurer ,ApplicationContextAware {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void addViewControllers(ViewControllerRegistry controllerRegistry) {

		controllerRegistry.addViewController("/").setViewName("login");
		controllerRegistry.addViewController("/login").setViewName("login");
		controllerRegistry.addViewController("/home").setViewName("home");
		controllerRegistry.addViewController("/dashboard").setViewName("dashboard");
		controllerRegistry.addViewController("/login").setViewName("login");
		controllerRegistry.addViewController("/users/signup").setViewName("signup");
		controllerRegistry.addViewController("/logout").setViewName("logout");
		controllerRegistry.addViewController("/admin/profile").setViewName("profile");
		controllerRegistry.addViewController("/role/roles").setViewName("roles");
		controllerRegistry.addViewController("/role/newRole").setViewName("newRole");
		controllerRegistry.addViewController("/error/403").setViewName("403");
		controllerRegistry.addViewController("/error/409").setViewName("409");
		controllerRegistry.addViewController("/error/404").setViewName("404");
		controllerRegistry.addViewController("/error/500").setViewName("500");
		controllerRegistry.addViewController("/users").setViewName("users");
		controllerRegistry.addViewController("/lastName").setViewName("lastname");
		controllerRegistry.addViewController("/admin").setViewName("homepage");
		controllerRegistry.addViewController("/admin/profile/email").setViewName("changeEmail");
		controllerRegistry.addViewController("/admin/password").setViewName("changePassword");
		controllerRegistry.addViewController("/access_denied").setViewName("accessDenied");

	}

	  @Bean
	    public ViewResolver viewResolver() {
	        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
	        resolver.setTemplateEngine(templateEngine());
	        resolver.setCharacterEncoding("UTF-8");
	        return resolver;
	    }

	    @Bean
	    public ISpringTemplateEngine templateEngine() {
	        SpringTemplateEngine engine = new SpringTemplateEngine();
	        engine.setEnableSpringELCompiler(true);
	        engine.setTemplateResolver(templateResolver());
	        engine.addDialect(new LayoutDialect());
	        return engine;
	    }

	    private ITemplateResolver templateResolver() {
	        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
	        resolver.setApplicationContext(applicationContext);
	        resolver.setPrefix("classpath:templates/");
	        resolver.setSuffix(".html");
	        resolver.setTemplateMode(TemplateMode.HTML);
	        return resolver;
	    }

	@Bean
	public ITemplateResolver svgTemplateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setPrefix("classpath:/templates/svg/");
		resolver.setSuffix(".svg");
		resolver.setTemplateMode("XML");
		resolver.setOrder(1);
		resolver.setCheckExistence(true);
		resolver.setCharacterEncoding("UTF-8");

		return resolver;
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
		formatterRegistry.addFormatter(new LocalDateTimeFormatter());

	}

	public void configurePathMatch(PathMatchConfigurer configurer) {
		UrlPathHelper urlPathHelper = new UrlPathHelper();
		urlPathHelper.setRemoveSemicolonContent(false);
		configurer.setUrlPathHelper(urlPathHelper);
	}

}
