package org.example;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public CommandLineRunner openApiGroup(SwaggerUiConfigParameters swaggerUiConfigParameters){
        return args -> {
            swaggerUiConfigParameters.addGroup("keycloak-admin-client");
        };
    }
}
