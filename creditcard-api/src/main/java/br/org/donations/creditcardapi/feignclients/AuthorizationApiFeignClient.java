package br.org.donations.creditcardapi.feignclients;

import br.org.donations.creditcardapi.config.security.FeignClientAuthInterceptor;
import br.org.donations.creditcardapi.dto.LoginRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(
        name = "authorization-api",
        contextId = "authorization-api",
        url = "${app-config.services.authorization-api}",
        path = "",
        configuration = FeignClientAuthInterceptor.class)
public interface AuthorizationApiFeignClient {
    @PostMapping("/token")
    String getAuthorizationToken(@RequestBody LoginRequest loginRequest);
}
