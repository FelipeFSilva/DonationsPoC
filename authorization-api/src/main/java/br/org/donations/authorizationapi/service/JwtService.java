package br.org.donations.authorizationapi.service;

import br.org.donations.authorizationapi.exception.AuthorizationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    private static final String BEARER = "Bearer ";

    @Value("${app-config.security.jwt.secret-key}")
    private String secretKey;

    public String generateToken(Authentication authentication) {
        Instant now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(now);
        Date expirationDate = Date.from(now.plus(30, ChronoUnit.SECONDS));

        return Jwts
                .builder()
                .setSubject(authentication.getName())
                .setIssuedAt(date)
                .setExpiration(expirationDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token) {
        try {
            LocalDateTime expirationLocalDate = getExpirationFromToken(token);
            return LocalDateTime.now().isBefore(expirationLocalDate);
        }catch (ExpiredJwtException ex){
            return false;
        }
    }

    private LocalDateTime getExpirationFromToken(String token) throws ExpiredJwtException{
        Claims claims = extractAllClaims(token);
        Date expirationDate = claims.getExpiration();
        return expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public String extractUserLogin(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) throws ExpiredJwtException{
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (SignatureException ex){
            throw new AuthorizationException("Token de acesso inválido.");
        }
    }

    public boolean isValidAuthorization(String authorization) {
        return authorization != null && authorization.startsWith(BEARER);
    }

    public String extractToken(String authorization) {
        return authorization.substring(7);
    }
}
