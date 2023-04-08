package br.org.donations.authorizationapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app-config.security.jwt.username}")
    private String username;
    @Value("${app-config.security.jwt.password}")
    private String password;

    @Override
    public UserDetails loadUserByUsername(String usernameToken) throws UsernameNotFoundException {
        if (!usernameToken.equals(username))
            throw new UsernameNotFoundException("Credenciais inválidas");

        return User.builder()
                .username(usernameToken)
                .password(passwordEncoder.encode(password))
                .authorities("USER_DONOR")
                .build();
    }
}
