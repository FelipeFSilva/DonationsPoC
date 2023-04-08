package br.org.donations.donationsapi.service;

import br.org.donations.donationsapi.exception.AuthenticationException;
import br.org.donations.donationsapi.exception.AuthorizationException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class JwtService {

    private static final String BEARER = "Bearer ";

    @Value("${app-config.security.jwt.secret-key}")
    private String secretKey;
    @Value("${app-config.security.jwt.username}")
    private String username;

    public void validateAuthorization(String authorization) {
        var accessToken = extractToken(authorization);
        try {
            var claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
            validateUsername(claims.getSubject());
        }catch (AuthenticationException ex) {
            throw ex;
        }catch (Exception ex) {
            throw new AuthorizationException("Erro enquanto tenta processar o Token de acesso.");
        }
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private void validateUsername(String usernameToken) {
        if (isEmpty(usernameToken) || !usernameToken.equals(username))
            throw new AuthenticationException("Usuário inválido");
    }

    private String extractToken(String authorization) {
        if (isEmpty(authorization))
            throw new AuthorizationException("Token de acesso não informado.");
        if (authorization.contains(BEARER))
            return authorization.substring(7);

        return authorization;
    }
}
