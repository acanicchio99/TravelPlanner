package com.academy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class AppConfig {
    
    @Bean
    public WebClient geoapifyWebClient() {
        return WebClient.builder()
            .baseUrl("https://api.geoapify.com")
            .build();
    }
    
    @Bean
    public WebClient weatherWebClient() {
        return WebClient.builder()
            .baseUrl("https://api.openweathermap.org/data/2.5")
            .build();
    }
    
    /**
     * Configurazione OpenAPI/Swagger per includere il Bearer Authentication
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Travel Planner API")
                        .version("1.0")
                        .description("API REST per la pianificazione di viaggi con autenticazione JWT"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Inserisci il token JWT ottenuto dal login (senza 'Bearer')")));
    }
}
