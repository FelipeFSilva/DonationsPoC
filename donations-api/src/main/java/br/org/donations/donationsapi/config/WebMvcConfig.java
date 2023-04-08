package br.org.donations.donationsapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app-config.services.billet-api}")
    private String billetHost;
    @Value("${app-config.services.creditcard-api}")
    private String creditCardHost;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/credit-card/validate")
                .allowedOrigins(billetHost, creditCardHost)
                .allowedHeaders(HttpHeaders.AUTHORIZATION)
                .allowedMethods(HttpMethod.POST.name());
    }
}
