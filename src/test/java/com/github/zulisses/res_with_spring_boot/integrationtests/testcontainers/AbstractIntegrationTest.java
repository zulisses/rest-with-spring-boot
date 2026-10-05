package com.github.zulisses.res_with_spring_boot.integrationtests.testcontainers;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.1");
        
        private static void startContainers() {
            Startables.deepStart(Stream.of(postgres)).join();
        }

        @Override
        public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource testcontainers = new MapPropertySource("testcontainers", 
                   Objects.requireNonNull(createConnectionConfiguration()));
            environment.getPropertySources().addFirst(testcontainers);
        }

        private static Map<String, Object> createConnectionConfiguration() {
            return Map.of(
                "spring.datasource.url", postgres.getJdbcUrl(),
                "spring.datasource.username", postgres.getUsername(),
                "spring.datasource.password", postgres.getPassword()
            );
        }
    }

}
