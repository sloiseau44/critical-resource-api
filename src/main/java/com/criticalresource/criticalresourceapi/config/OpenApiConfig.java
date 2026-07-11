package com.criticalresource.criticalresourceapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        String version = getClass().getPackage().getImplementationVersion();
        if (version == null) version = "dev";

        return new OpenAPI()
                .info(new Info()
                        .title("Critical Resource API")
                        .description("API REST de gestion de ressources critiques")
                        .version(version))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Auth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Auth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}