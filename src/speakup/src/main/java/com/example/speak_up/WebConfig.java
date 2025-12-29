package com.example.speak_up;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // apply to all paths
                        .allowedOrigins("http://localhost:3000") // allow React frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // allow common HTTP methods
                        .allowedHeaders("*"); // allow all headers
            }
        };
    }
}
