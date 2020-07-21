package com.exalt.ipc.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
	public static final String TITLE = "IPC-API";

	public static final String DESCRIPTION = "An application to simulate IPC service";

	public static final String VERSION = "IPC-API v1";

	public static final String TERMS_OF_SERVICE_URL = "Terms of service";

	public static final String CONTACT_NAME = "Mohammad Hasan";

	public static final String CONTACT_URL = "http://www.exalt-tech.com/";

	public static final String CONTACT_EMAIL = "mohammad19991998dtrb@gmail.com";

	public static final String LICENSE = "License of API";

	public static final String LICENSE_URL = "https://swagger.io/docs/";

	public static final String BASE_PKG = "com.exalt.ipc";

	public static final SortedSet<String> produces = new TreeSet<>();


	@Bean
	public Docket api() {
		produces.add("application/json");
		produces.stream().forEach(System.out::println);
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage(BASE_PKG))
																									.paths(PathSelectors.any()).build().apiInfo(apiInfo())
																									.consumes(Collections.singleton("application/json"))
																									.produces(produces).useDefaultResponseMessages(false)
																									.globalResponseMessage(RequestMethod.GET, newArrayList(
																											new ResponseMessageBuilder().code(500).message("500 message")
																																									//add it if you want specific model
																																									//.responseModel(new ModelRef("Error"))
																																									.build(),
																											new ResponseMessageBuilder().code(403).message("Forbidden!")
																																									.build()));
	}

	private ApiInfo apiInfo() {
		ApiInfo apiInfo = new ApiInfo(TITLE, DESCRIPTION, VERSION, TERMS_OF_SERVICE_URL,
				new Contact(CONTACT_NAME, CONTACT_URL, CONTACT_EMAIL), LICENSE, LICENSE_URL, Collections.emptyList());
		return apiInfo;
	}
}
