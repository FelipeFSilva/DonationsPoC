package br.org.donations.creditcardapi.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app-config.services.authorization-api}")
    private String authorizationApi;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/creditcard/send-donation")
                .allowedOrigins(authorizationApi)
                .allowedHeaders(HttpHeaders.AUTHORIZATION)
                .allowedMethods(HttpMethod.POST.name());
    }
}
