package br.org.donations.billetapi.utils;

import br.org.donations.billetapi.dto.*;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TestUtils {

    private static String BILLET_SEND_DONATION_URI = "/billet/send-donation";
    private static String BILLET_GET_DONATION_URI = "/billet/get-donations";
    public static final String TOKEN_URI = "/token";
    public static final String APPROVED = "APPROVED";
    public static final String EMAIL = "teste@email.com";
    public static final String BEARER = "Bearer ";
    public static final long EXPIRATION_TOKEN = 30;
    public static final String SECRET_KEY = "umlUZ92WEn4ROu0OUmV42Np+XMfPfWiHKx7oZNxkcErpON5tsBLxNMZLwaRrQ+c8cyPmt6RgaZ0tW2LZZDVhbQ==";
    public static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkb25vci11c2VyIiwiaWF0IjoxNjc2MDQwMDU2LCJleHAiOjE2NzYwNDAwODZ9.pIbc-bZaEZKV3cqvOrGxJ3AqALnlwmxf-i1CKSUMFfpW-gjA_a5LZqyrGzO0cmuFBy1kvP4K3ka0Bnz3vj80lw";
    public static final String USER_LOGIN = "donor-user";
    public static final String PASSWORD = "ZG9Bw6fDo28xMjNAIQ";
    private static ObjectMapper objectMapper;

    public static BilletDonationDTO createDonationInputDTO() {
        return BilletDonationDTO.builder()
                .donor(DonorDTO.builder()
                        .name("Fulano")
                        .documentNumber("00011122233")
                        .email("teste@email.com").build())
                .address(AddressDTO.builder()
                        .addressNumber("12345678")
                        .city("cidade")
                        .number(100)
                        .state("RR")
                        .street("rua maneira")
                        .build())
                .value(BigDecimal.valueOf(100.00))
                .build();
    }

    public static MockHttpServletRequestBuilder sendBilletDonationPostRequest(String json) {
        return MockMvcRequestBuilders
                .post(BILLET_SEND_DONATION_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
    }

    public static MockHttpServletRequestBuilder getSendDonationPostRequestWithToken(String json, String token) {
        return MockMvcRequestBuilders
                .post(BILLET_SEND_DONATION_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BEARER + token)
                .content(json);
    }

    public static MockHttpServletRequestBuilder getSendDonationGetRequestWithToken(String json, String token) {
        return MockMvcRequestBuilders
                .get(BILLET_GET_DONATION_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BEARER + token)
                .content(json);
    }

    public static DonorResponse createValidDonorResponse(){
        return DonorResponse.builder()
                .name("Fulano")
                .type("PF")
                .documentNumber("00011122233")
                .email(EMAIL)
                .build();
    }

    public static BilletDonationResponseDTO createValidDonationToSaveDTO(){
        LocalDateTime dateTimeNow = LocalDateTime.now();
        return BilletDonationResponseDTO.builder()
                .valuePaid(BigDecimal.valueOf(100.00))
                .status(APPROVED)
                .createdAt(dateTimeNow)
                .updatedAt(dateTimeNow)
                .donor(createValidDonorResponse())
                .build();
    }

    public static String getJson(ObjectMapper objectMapper, BilletDonationDTO donationDTO) throws JsonProcessingException {
        return objectMapper.writeValueAsString(donationDTO);
    }

    public static String generateToken() throws Exception {
        Instant now = getInstantNow();
        return Jwts
                .builder()
                .setSubject(USER_LOGIN)
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


}
