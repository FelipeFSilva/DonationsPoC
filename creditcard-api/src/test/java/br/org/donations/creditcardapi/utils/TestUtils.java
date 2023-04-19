package br.org.donations.creditcardapi.utils;

import br.org.donations.creditcardapi.dto.*;
import br.org.donations.creditcardapi.model.Donation;
import br.org.donations.creditcardapi.model.Donor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
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

public class TestUtils {

    public static final String CREDIT_CARD_SEND_DONATION_URI = "/creditcard/send-donation";
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


    public static DonationDTO createDonationInputDTO() {
        return DonationDTO.builder()
                .donor(DonorDTO.builder()
                        .name("Fulano")
                        .documentNumber("00011122233")
                        .email(EMAIL).build())
                .creditCard(CreditCardDTO.builder()
                        .holder("Fulano Teste")
                        .number("5557892195046665")
                        .securityCode("123")
                        .validity(LocalDate.now().plusYears(1)).build())
                .value(BigDecimal.valueOf(100.00))
                .build();
    }

    public static MockHttpServletRequestBuilder getSendDonationPostRequestWithToken(String json, String token) {
        return MockMvcRequestBuilders
                .post(CREDIT_CARD_SEND_DONATION_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BEARER + token)
                .content(json);
    }

    public static MockHttpServletRequestBuilder getSendDonationPostRequest(String json) {
        return MockMvcRequestBuilders
                .post(CREDIT_CARD_SEND_DONATION_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
    }

    public static MockHttpServletRequestBuilder getTokenRequestPost(String json) throws JsonProcessingException {
        return MockMvcRequestBuilders.post(TOKEN_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
    }

    public static String getTokenRequestJson(LoginRequest loginRequest) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(loginRequest);
    }

    public static String getJson(DonationDTO donationDTO) throws JsonProcessingException {
        objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(donationDTO);
    }

    public static LoginRequest createValidLoginRequest() {
        return LoginRequest.builder().username(USER_LOGIN).password(PASSWORD).build();
    }

    public static Donor createValidDonorEntity(){
        return Donor.builder()
                .name("Fulano")
                .type("PF")
                .documentNumber("00011122233")
                .email(EMAIL)
                .build();
    }


    public static Donation createValidDonationEntity(){
        LocalDateTime dateTimeNow = LocalDateTime.now();
        return Donation.builder()
                .valuePaid(BigDecimal.valueOf(100.00))
                .status(APPROVED)
                .createdAt(dateTimeNow)
                .updatedAt(dateTimeNow)
                .donor(createValidDonorEntity())
                .build();
    }

    public static DonorToSaveDTO createValidDonorToSaveDTO(){
        return DonorToSaveDTO.builder()
                .name("Fulano")
                .type("PF")
                .documentNumber("00011122233")
                .email(EMAIL)
                .build();
    }

    public static DonationToSaveDTO createValidDonationToSaveDTO(){
        LocalDateTime dateTimeNow = LocalDateTime.now();
        return DonationToSaveDTO.builder()
                .valuePaid(BigDecimal.valueOf(100.00))
                .status(APPROVED)
                .createdAt(dateTimeNow)
                .updatedAt(dateTimeNow)
                .donor(createValidDonorToSaveDTO())
                .build();
    }

    public static String generateAnyStringWithLength(int length){
        StringBuilder generatedString = new StringBuilder();
        for (int i = 0; i < length; i++){
            generatedString.append("*");
        }
        return generatedString.toString();
    }

    public static EmailDTO createValidEmailDTO(){
        return EmailDTO.builder()
                .donorName("Fulano")
                .documentNumber("***111222**")
                .typePerson("PF")
                .email(EMAIL)
                .statusDonation(APPROVED)
                .donationValue(BigDecimal.valueOf(10.00))
                .donationCreatedAt(LocalDateTime.now())
                .build();
    }

    public static String generateToken() throws Exception {
        LoginRequest validLoginRequest = createValidLoginRequest();
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
