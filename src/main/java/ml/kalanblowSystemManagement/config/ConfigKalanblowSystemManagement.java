package ml.kalanblowSystemManagement.config;

import java.util.Arrays;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.NamingConventions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SuppressWarnings("unused")
@Configuration
@EnableSwagger2
public class ConfigKalanblowSystemManagement {

	@Bean
	public ModelMapper modelMapper() {

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setFieldAccessLevel(AccessLevel.PRIVATE).setFieldMatchingEnabled(true)
		.setSourceNamingConvention(NamingConventions.JAVABEANS_MUTATOR).setSkipNullEnabled(true);
		return modelMapper;

	}

	@Bean
	public Docket allApi() {

		return new Docket(DocumentationType.SWAGGER_2).groupName("All APIs").select()
				.apis(RequestHandlerSelectors.basePackage("ml.kalanblowSystemManagement.controller.web.api"))
				.paths(PathSelectors.any()).build().apiInfo(apiInfo()).securitySchemes(Arrays.asList(apiKey()));
	}

	@Bean
	public Docket customerApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Admin APIs")
				.select()
				.apis(RequestHandlerSelectors.basePackage("ml.kalanblowSystemManagement.controller.web.api"))
				.paths(PathSelectors.regex("/api/v1/user.*"))
				.build()
				.apiInfo(apiInfo());
	}

	private ApiKey apiKey() {

		return new ApiKey("apiKey", "Authorization", "header");
	}

	private ApiInfo apiInfo() {

		return new ApiInfoBuilder().title("Kalanblow School- REST APIs").description("Kalanblow application")
				.termsOfServiceUrl("").contact(new Contact("Guindo Amadou", null, "amguindo77@gmail.com"))
				.license("Apache Licence Version 2.0").licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
				.version("1.0").build();
	}
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
