package com.formidex.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig {
    @Bean
    public Docket swaggerConfig(){

        return  new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .build()
                .apiInfo(apiDetails());

    }
    private ApiInfo apiDetails(){
        return new ApiInfo(
                "Formidex Forex APi",
                "API for calculating foreign exchange values on given dates and money exchange values based on the reference",
                "1.0",
                "Private use",
                new Contact("Formidex", "", "tlebeya57@gmail.com"),
                "API license",
                "",
                Collections.emptyList()
        );
    }
}
