package br.org.donations.donationsapi.config;

import br.org.donations.donationsapi.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public JwtService jwtService() {
        return new JwtService();
    }

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor(jwtService());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor());
    }
}
