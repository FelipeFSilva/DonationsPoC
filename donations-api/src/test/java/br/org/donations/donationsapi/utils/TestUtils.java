package br.org.donations.donationsapi.utils;

import br.org.donations.donationsapi.dto.*;
import br.org.donations.donationsapi.enums.TypePersonEnum;
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

public class TestUtils {

    public static String DONATION_CREDIT_CARD_VALIDATE_URI = "/credit-card/validate";
    public static String DONATION_BILLET_VALIDATE_URI = "/billet/validate";

    public static final String BEARER = "Bearer ";
    public static final long EXPIRATION_TOKEN = 30;
    public static final String USER_LOGIN = "donor-user";
    public static final String SECRET_KEY = "umlUZ92WEn4ROu0OUmV42Np+XMfPfWiHKx7oZNxkcErpON5tsBLxNMZLwaRrQ+c8cyPmt6RgaZ0tW2LZZDVhbQ==";


    public static MockHttpServletRequestBuilder getValidateCreditCardDonationPostRequestWithToken(String json, String token) {
        return MockMvcRequestBuilders
                .post(DONATION_CREDIT_CARD_VALIDATE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BEARER + token)
                .content(json);
    }

    public static MockHttpServletRequestBuilder getValidateBilletDonationPostRequest(String json, String token) {
        return MockMvcRequestBuilders
                .post(DONATION_BILLET_VALIDATE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BEARER + token)
                .content(json);
    }

    public static DonationCreditCardDTO createValidDonationDTO() {
        return DonationCreditCardDTO.builder()
                .donor(DonorDTO.builder()
                        .name("Fulano")
                        .documentNumber("00011122233")
                        .email("teste@email.com").build())
                .creditCard(CreditCardDTO.builder()
                        .holder("Fulano Teste")
                        .number("5557892195046665")
                        .securityCode("123")
                        .validity(LocalDate.now().plusYears(1)).build())
                .value(BigDecimal.valueOf(100.00))
                .build();
    }

    public static DonorResponse createDonorResponse() {
        return DonorResponse.builder()
                .name("Fulano")
                .type(TypePersonEnum.PF.toString())
                .documentNumber("123******22")
                .email("teste@email.com")
                .build();
    }

    public static DonationResponse createDonationResponse(){
        LocalDateTime dateTimeNow = LocalDateTime.now();
        return DonationResponse.builder()
                .valuePaid(BigDecimal.valueOf(100.00))
                .status("PENDING")
                .createdAt(dateTimeNow)
                .updatedAt(dateTimeNow)
                .donor(createDonorResponse())
                .build();
    }

    public static BilletDonationResponse createBilletDonationResponse(){
        LocalDateTime dateTimeNow = LocalDateTime.now();
        return BilletDonationResponse.builder()
                .valuePaid(BigDecimal.valueOf(100.00))
                .status("PENDING")
                .createdAt(dateTimeNow)
                .updatedAt(dateTimeNow)
                .donor(createDonorResponse())
                .link("http://boleto.api.fake/00011122233")
                .build();
    }

    public static DonationBilletDTO createValidBilletDonationDTO() {
        return DonationBilletDTO.builder()
                .donor(DonorDTO.builder()
                        .name("Fulano")
                        .documentNumber("00011122233")
                        .email("teste@email.com").build())
                .address(BilletAddressDTO.builder()
                        .city("cidade")
                        .addressNumber("00000-000")
                        .street("rua")
                        .number(100)
                        .state("PR")
                        .build())
                .value(BigDecimal.valueOf(100.00))
                .build();
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

}
