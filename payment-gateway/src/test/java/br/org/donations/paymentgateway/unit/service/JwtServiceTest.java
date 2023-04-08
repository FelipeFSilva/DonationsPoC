package br.org.donations.paymentgateway.unit.service;

import br.org.donations.paymentgateway.exception.AuthenticationException;
import br.org.donations.paymentgateway.exception.AuthorizationException;
import br.org.donations.paymentgateway.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import static br.org.donations.paymentgateway.utils.TestCreditCardUtils.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;
    private String token;

    @BeforeEach
    public void setUp() {
        token = generateToken();
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtService, "username", USER_LOGIN);
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_KEY);
    }

    @Test
    @DisplayName("Deve validar uma authorization com sucesso")
    public void validateAuthorizationTest() throws Exception {
        token = generateToken();
        Throwable throwable = org.assertj.core.api.Assertions.catchThrowable(() -> jwtService.validateAuthorization(BEARER + token));
        assertThat(throwable).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Deve lançar exceção quando encaminhar token gerado a partir de usuário inválido")
    public void invalidUserTokenTest() throws Exception {
        token = generateTokenWithInvalidUser();

        Throwable throwable = org.assertj.core.api.Assertions.catchThrowable(() -> jwtService.validateAuthorization(BEARER + token));
        assertThat(throwable).isInstanceOf(AuthenticationException.class);

    }

    @Test
    @DisplayName("Deve lançar exceção quando encaminhar token com formato invalido")
    public void invalidTokenTest() throws Exception {

        Throwable throwable = org.assertj.core.api.Assertions.catchThrowable(() -> jwtService.validateAuthorization(token.substring(7)));
        assertThat(throwable).isInstanceOf(AuthorizationException.class);

    }
}
