package br.org.donations.authorizationapi.controller;

import br.org.donations.authorizationapi.dto.LoginRequest;
import br.org.donations.authorizationapi.exception.AuthenticationException;
import br.org.donations.authorizationapi.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "AuthController")
@RestController
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/token")
    public String getToken(@RequestBody LoginRequest userLogin) throws AuthenticationException {
        try {
            log.info("Iniciando getToken");
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));
            return jwtService.generateToken(authentication);
        }
        catch (Exception ex){
            throw new AuthenticationException("Não foi possível realizar o login com os dados informados");
        }
    }
}
