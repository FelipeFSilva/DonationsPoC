package br.org.donations.authorizationapi.config.security;

import br.org.donations.authorizationapi.service.JwtService;
import br.org.donations.authorizationapi.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetails;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        boolean isValidAuthorization = jwtService.isValidAuthorization(authorization);

        if (isValidAuthorization){
            String token = jwtService.extractToken(authorization);

            if(jwtService.isTokenValid(token)){
                String userLogin = jwtService.extractUserLogin(token);
                UserDetails user = userDetails.loadUserByUsername(userLogin);
                UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken(user,null, user.getAuthorities());
                userAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(userAuthentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
