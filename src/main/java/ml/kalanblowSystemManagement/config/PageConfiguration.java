
package ml.kalanblowSystemManagement.config;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.UrlPathHelper;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import ml.kalanblowSystemManagement.utils.FrenchLocalDateFormater;
import ml.kalanblowSystemManagement.utils.LocalDateTimeFormatter;

@Configuration
public class PageConfiguration implements WebMvcConfigurer {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

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
        controllerRegistry.addViewController("/editeUser").setViewName("editeUser");
        // controllerRegistry.addViewController("/editeUser/**").setViewName("updateUser");
        controllerRegistry.addViewController("/deleteUser/**").setViewName("deleteUser");
        controllerRegistry.addViewController("/lastName").setViewName("lastname");
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

    @Bean
    public FilterRegistrationBean<HiddenHttpMethodFilter> hiddenHttpMethodFilter() {
        FilterRegistrationBean<HiddenHttpMethodFilter> filterRegBean =
                new FilterRegistrationBean<HiddenHttpMethodFilter>(new HiddenHttpMethodFilter());
        filterRegBean.setUrlPatterns(Arrays.asList("/*"));
        return filterRegBean;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /*
     * @Bean public LocalValidatorFactoryBean getValidator() { LocalValidatorFactoryBean bean = new
     * LocalValidatorFactoryBean(); bean.setValidationMessageSource(messageSource()); return bean; }
     */

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.getDefault());
        return slr;
    }

    /*
     * @Bean public Formatter<LocalDateTime> localDateTimeFormatter() { return new
     * Formatter<LocalDateTime>() {
     * 
     * @Override public LocalDateTime parse(String text, Locale locale) throws ParseException {
     * return LocalDateTime.parse(text, DATE_TIME_FORMATTER); }
     * 
     * @Override public String print(LocalDateTime object, Locale locale) { return
     * DATE_TIME_FORMATTER.format(object); } }; }
     */

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
    
      @Override public void addFormatters(FormatterRegistry formatterRegistry) {
      
      formatterRegistry.addFormatterForFieldType(LocalDate.class, new FrenchLocalDateFormater());
      formatterRegistry.addFormatter(new LocalDateTimeFormatter());
      
      }
     

    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        PageableHandlerMethodArgumentResolver resolver =
                new PageableHandlerMethodArgumentResolver();
        resolver.setPageParameterName("page");
        resolver.setSizeParameterName("size");
        resolver.setOneIndexedParameters(false);
        resolver.setFallbackPageable(PageRequest.of(0, 50));
        resolver.setMaxPageSize(100);
        resolvers.add(resolver);
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }

}
