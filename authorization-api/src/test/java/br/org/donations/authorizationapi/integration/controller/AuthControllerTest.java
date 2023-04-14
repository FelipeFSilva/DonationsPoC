package br.org.donations.authorizationapi.integration.controller;

import br.org.donations.authorizationapi.AuthorizationApiApplication;
import br.org.donations.authorizationapi.dto.LoginRequest;
import br.org.donations.authorizationapi.service.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static br.org.donations.authorizationapi.utils.TestUtils.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AuthorizationApiApplication.class)
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@ImportAutoConfiguration
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    private JwtService jwtService;

    @Test
    @DisplayName("Deve retornar status 'Unauthorized'")
    public void invalidCredentialsTest() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder().username("teste").password("teste").build();
        String json = getTokenRequestJson(loginRequest);

        MockHttpServletRequestBuilder request = getTokenRequestPost(json);
        mvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar token com sucesso")
    public void getTokenTest() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder().username(USER_LOGIN).password(PASSWORD).build();
        String json = getTokenRequestJson(loginRequest);

        MockHttpServletRequestBuilder request = getTokenRequestPost(json);
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }
}
