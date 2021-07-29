package ml.kalanblowSystemManagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
		controllerRegistry.addViewController("/etablissement").setViewName("etablissement");
		controllerRegistry.addViewController("/users").setViewName("users");
		// controllerRegistry.addViewController("/allUsers").setViewName("allUsers");
		// controllerRegistry.addViewController("/createUserForm").setViewName("createUserForm");
		controllerRegistry.addViewController("/getUser").setViewName("getUser");
		controllerRegistry.addViewController("/updateUser").setViewName("updateUser");
		controllerRegistry.addViewController("/student/registration").setViewName("registration");
		controllerRegistry.addViewController("/student/edit").setViewName("student-edit");
		controllerRegistry.addViewController("/student/delete").setViewName("student-delete");
	}
}
