package br.org.donations.creditcardapi.config.security;

import br.org.donations.creditcardapi.dto.LoginRequest;
import br.org.donations.creditcardapi.feignclients.AuthorizationApiFeignClient;
import br.org.donations.creditcardapi.service.JwtService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeignClientInterceptor implements RequestInterceptor {

    private static final String BEARER = "Bearer ";
    private final JwtService jwtService;

    @Autowired
    private AuthorizationApiFeignClient authorizationApiFeignClient;

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
        else {
            String accessToken = authorizationApiFeignClient.getAuthorizationToken(new LoginRequest(username, password));
            requestTemplate.header(HttpHeaders.AUTHORIZATION);
            requestTemplate.header(HttpHeaders.AUTHORIZATION, BEARER + accessToken);
        }
    }
}
