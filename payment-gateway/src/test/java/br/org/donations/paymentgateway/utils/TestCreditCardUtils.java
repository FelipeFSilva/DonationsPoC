package br.org.donations.paymentgateway.utils;

import br.org.donations.paymentgateway.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TestCreditCardUtils {

    public static final String PAYMENT_CREDIT_CARD_URI = "/credit-card/validate-payment";
    public static final String PAYMENT_BILLET_URI = "/billet/validate-payment";
    public static final String BEARER = "Bearer ";
    public static final long EXPIRATION_TOKEN = 30;
    public static final String USER_LOGIN = "donor-user";
    public static final String SECRET_KEY = "umlUZ92WEn4ROu0OUmV42Np+XMfPfWiHKx7oZNxkcErpON5tsBLxNMZLwaRrQ+c8cyPmt6RgaZ0tW2LZZDVhbQ==";

    public static PaymentCreditCardDTO createValidPaymentCreditCardDTO() {
        return PaymentCreditCardDTO.builder()
                .creditCard(CreditCardDTO.builder()
                        .holder("Maria")
                        .number("5557892195065210")
                        .securityCode("222")
                        .validity(LocalDate.of(2025, 01, 01))
                        .build())
                .value(BigDecimal.valueOf(10.00))
                .build();
    }

    public static PaymentBilletDTO createValidPaymentBilletDTO() {
        return PaymentBilletDTO.builder()
                .donor(DonorDTO.builder()
                        .documentNumber("43066629007")
                        .email("teste@teste.com")
                        .name("Fulano Pereira")
                        .build())
                .address(AddressDTO.builder()
                        .city("Cidade")
                        .number(100)
                        .state("PR")
                        .addressNumber("00000-000")
                        .street("Rua")
                        .build())
                .value(BigDecimal.valueOf(10.00))
                .build();
    }
    public static MockHttpServletRequestBuilder getPostRequest(String json, String token){
        return MockMvcRequestBuilders.post(PAYMENT_CREDIT_CARD_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BEARER + token)
                .content(json);
    }

    public static MockHttpServletRequestBuilder getBilletPostRequest(String json) {
        return MockMvcRequestBuilders.post(PAYMENT_BILLET_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
    }

    public static String getJson(PaymentCreditCardDTO paymentCreditCardDTO) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(paymentCreditCardDTO);
    }

    public static String generateToken() {
        Instant now = getInstantNow();
        return Jwts
                .builder()
                .setSubject(USER_LOGIN)
                .setIssuedAt(getDateFromInstant(now))
                .setExpiration(getExpirationDate(now))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public static String generateTokenWithInvalidUser() {
        Instant now = getInstantNow();
        return Jwts
                .builder()
                .setSubject("teste")
                .setIssuedAt(getDateFromInstant(now))
                .setExpiration(getExpirationDate(now))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private static Instant getInstantNow() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
    }

    private static Date getDateFromInstant(Instant now) {
        return Date.from(now);
    }

    private static Date getExpirationDate(Instant now) {
        return Date.from(now.plus(EXPIRATION_TOKEN, ChronoUnit.SECONDS));
    }

    private static Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static String getBilletJson(PaymentBilletDTO paymentBilletDTO) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(paymentBilletDTO);
    }
}
