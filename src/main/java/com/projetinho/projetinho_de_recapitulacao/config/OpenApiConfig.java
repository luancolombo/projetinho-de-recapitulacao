package com.projetinho.projetinho_de_recapitulacao.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("REST API RESTful from 0 with Java, Spring Boot, Kubernetes and Docker")
                        .version("v1")
                        .description("REST API RESTful from 0 with Java, Spring Boot, Kubernetes and Docker")
                        .termsOfService("https://github.com/luancolombo")
                        .license(new License()
                                .name("MIT")
                                .url("https://github.com/luancolombo/projetinho-de-recapitulacao?tab=MIT-1-ov-file")
                        )
                );
    }
}
