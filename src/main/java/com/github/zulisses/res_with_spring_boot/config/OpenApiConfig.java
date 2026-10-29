package com.github.zulisses.res_with_spring_boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("REST API's RESTful from 0 with Java, Spring boot, Kubernetes and Docker - v2")
                .version("v1")
                .description("API documentation for the project")
                .termsOfService("https://github.com/zulisses/res-with-spring-boot")
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://github.com/zulisses/res-with-spring-boot/blob/main/LICENSE")));
    }
}
 