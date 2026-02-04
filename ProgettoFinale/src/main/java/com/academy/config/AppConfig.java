package com.academy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

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
}
