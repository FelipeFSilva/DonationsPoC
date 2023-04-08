package br.org.donations.authorizationapi.config.security;

import br.org.donations.authorizationapi.service.JwtService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FeignClientInterceptor implements RequestInterceptor {
    private static final String BEARER = "Bearer ";
    private final JwtService jwtService;

    @Value("${app-config.security.jwt.username}")
    private String username;
    @Value("${app-config.security.jwt.password}")
    private String password;

    public FeignClientInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    private static final Pattern BEARER_TOKEN_HEADER_PATTERN = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+=*)$",
            Pattern.CASE_INSENSITIVE);

    @Override
    public void apply(RequestTemplate requestTemplate) {
        final String authorization = HttpHeaders.AUTHORIZATION;
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            String authorizationHeader = requestAttributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
            Matcher matcher = BEARER_TOKEN_HEADER_PATTERN.matcher(authorizationHeader);
            if (matcher.matches()) {
                requestTemplate.header(authorization);
                requestTemplate.header(authorization, authorizationHeader);
            }
        }
    }
}
