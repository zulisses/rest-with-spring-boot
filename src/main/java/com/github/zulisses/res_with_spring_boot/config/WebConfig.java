package com.github.zulisses.res_with_spring_boot.config;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Value("${cors.originPattern:default}")
    private String corsOriginPatterns = "";

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        var allowedOrigins = corsOriginPatterns.split(",");
        registry.addMapping("/**")
            .allowedOrigins(Objects.requireNonNull(allowedOrigins))
            .allowedMethods("*")
            .allowCredentials(true);
    }

	@Override 
    public void configureContentNegotiation(@NonNull ContentNegotiationConfigurer configurer) {

        // via HEADER PARAMETER http://localhost:8080/api/person/v1
        configurer.favorParameter(false)
        .ignoreAcceptHeader(false)
        .useRegisteredExtensionsOnly(false)
        .defaultContentType(MediaType.APPLICATION_JSON)
        .mediaType("json", Objects.requireNonNull(MediaType.APPLICATION_JSON))
        .mediaType("xml", Objects.requireNonNull(MediaType.APPLICATION_XML))
        .mediaType("yaml", MediaType.parseMediaType("application/yaml"));
    }
}
