package br.org.donations.authorizationapi.utils;

import br.org.donations.authorizationapi.dto.CreditCard.CreditCardDTO;
import br.org.donations.authorizationapi.dto.CreditCard.DonationDTO;
import br.org.donations.authorizationapi.dto.DonorDTO;
import br.org.donations.authorizationapi.dto.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestUtils {

    public static final String CREDIT_CARD_SEND_DONATION_URI = "/creditcard/send-donation";
    public static final String TOKEN_URI = "/token";

    public static final String APPROVED = "APPROVED";
    public static final String EMAIL = "teste@email.com";
    public static final String BEARER = "Bearer ";
    public static final String SECRET_KEY = "umlUZ92WEn4ROu0OUmV42Np+XMfPfWiHKx7oZNxkcErpON5tsBLxNMZLwaRrQ+c8cyPmt6RgaZ0tW2LZZDVhbQ==";
    public static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkb25vci11c2VyIiwiaWF0IjoxNjc2MDQwMDU2LCJleHAiOjE2NzYwNDAwODZ9.pIbc-bZaEZKV3cqvOrGxJ3AqALnlwmxf-i1CKSUMFfpW-gjA_a5LZqyrGzO0cmuFBy1kvP4K3ka0Bnz3vj80lw";
    public static final String USER_LOGIN = "donor-user";
    public static final String PASSWORD = "ZG9Bw6fDo28xMjNAIQ";


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

    public static LoginRequest createValidLoginRequest() {
        return LoginRequest.builder().username(USER_LOGIN).password(PASSWORD).build();
    }

}
