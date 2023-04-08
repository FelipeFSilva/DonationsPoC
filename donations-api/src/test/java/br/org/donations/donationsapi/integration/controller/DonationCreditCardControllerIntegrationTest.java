package br.org.donations.donationsapi.integration.controller;

import br.org.donations.donationsapi.DonationsApiApplication;
import br.org.donations.donationsapi.dto.DonationCreditCardDTO;
import br.org.donations.donationsapi.dto.PaymentCreditCardDTO;
import br.org.donations.donationsapi.enums.CreditCardStatusEnum;
import br.org.donations.donationsapi.enums.DonationStatusEnum;
import br.org.donations.donationsapi.enums.TypePersonEnum;
import br.org.donations.donationsapi.feignclients.PaymentGatewayFeignClient;
import br.org.donations.donationsapi.service.DonationCreditCardService;
import br.org.donations.donationsapi.utils.DocumentNumberUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RetryableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static br.org.donations.donationsapi.utils.TestUtils.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@ImportAutoConfiguration
@SpringBootTest(classes = DonationsApiApplication.class)
public class DonationCreditCardControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private PaymentGatewayFeignClient paymentGatewayFeignClient;
    @Autowired
    private DonationCreditCardService donationCreditCardService;
    @Autowired
    private ObjectMapper objectMapper;
    private DonationCreditCardDTO donationDTO;
    private String token;

    @BeforeEach
    public void setUp() {
        donationDTO = createValidDonationDTO();
        token = generateToken();
    }

    @Test
    @DisplayName("Deve retornar uma doação aprovada.")
    public void validateCreditCardDonationIntegrationTest() throws Exception {
        Mockito.when(paymentGatewayFeignClient.paymentCreditCard(Mockito.any(PaymentCreditCardDTO.class)))
                .thenReturn(CreditCardStatusEnum.VALID.getStatus());

        String json = objectMapper.writeValueAsString(donationDTO);
        MockHttpServletRequestBuilder request = getValidateCreditCardDonationPostRequestWithToken(json, token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.donor.name").value(donationDTO.getDonor().getName()))
                .andExpect(jsonPath("$.donor.type").value(TypePersonEnum.PF.toString()))
                .andExpect(jsonPath("$.donor.email").value(donationDTO.getDonor().getEmail()))
                .andExpect(jsonPath("$.status").value(DonationStatusEnum.APPROVED.toString()))
                .andExpect(jsonPath("$.valuePaid").value(donationDTO.getValue()))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty());
    }

    @Test
    @DisplayName("Deve retornar uma doação rejeitada quando cartão com bandeira recusada.")
    public void validateCreditCardDonationWithCardFlagDeclinedTest() throws Exception {
        Mockito.when(paymentGatewayFeignClient.paymentCreditCard(Mockito.any(PaymentCreditCardDTO.class)))
                .thenReturn(CreditCardStatusEnum.FLAG_DECLINED.getStatus());

        String json = objectMapper.writeValueAsString(donationDTO);
        MockHttpServletRequestBuilder request = getValidateCreditCardDonationPostRequestWithToken(json, token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(DonationStatusEnum.REJECTED.toString()));
    }

    @Test
    @DisplayName("Deve retornar uma doação rejeitada quando cartão expirado.")
    public void validateCreditCardDonationWithCardValidityExpiredTest() throws Exception {
        Mockito.when(paymentGatewayFeignClient.paymentCreditCard(Mockito.any(PaymentCreditCardDTO.class)))
                .thenReturn(CreditCardStatusEnum.EXPIRED.getStatus());

        String json = objectMapper.writeValueAsString(donationDTO);
        MockHttpServletRequestBuilder request = getValidateCreditCardDonationPostRequestWithToken(json, token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(DonationStatusEnum.REJECTED.toString()));
    }

    @Test
    @DisplayName("Deve retornar uma doação rejeitada quando cartão com número inválido.")
    public void validateCreditCardDonationWithCardNumberInvalidTest() throws Exception {
        Mockito.when(paymentGatewayFeignClient.paymentCreditCard(Mockito.any(PaymentCreditCardDTO.class)))
                .thenReturn(CreditCardStatusEnum.INVALID_NUMBER.getStatus());

        String json = objectMapper.writeValueAsString(donationDTO);
        MockHttpServletRequestBuilder request = getValidateCreditCardDonationPostRequestWithToken(json, token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(DonationStatusEnum.REJECTED.toString()));
    }

    @Test
    @DisplayName("Deve retornar uma doação rejeitada quando cartão sem limite.")
    public void validateCreditCardDonationWithCardNoLimitTest() throws Exception {
        Mockito.when(paymentGatewayFeignClient.paymentCreditCard(Mockito.any(PaymentCreditCardDTO.class)))
                .thenReturn(CreditCardStatusEnum.NO_LIMIT.getStatus());

        String json = objectMapper.writeValueAsString(donationDTO);
        MockHttpServletRequestBuilder request = getValidateCreditCardDonationPostRequestWithToken(json, token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(DonationStatusEnum.REJECTED.toString()));
    }

    @Test
    @DisplayName("Deve retornar uma doação com CPF de doador ocultado")
    public void validateCreditCardDonationDocumentNumberCPFTest() throws Exception {
        Mockito.when(paymentGatewayFeignClient.paymentCreditCard(Mockito.any(PaymentCreditCardDTO.class)))
                .thenReturn(CreditCardStatusEnum.VALID.getStatus());
        String hiddenCPF = DocumentNumberUtils.hideCPFNumbers(donationDTO.getDonor().getDocumentNumber());

        String json = objectMapper.writeValueAsString(donationDTO);
        MockHttpServletRequestBuilder request = getValidateCreditCardDonationPostRequestWithToken(json, token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.donor.documentNumber").value(hiddenCPF));
    }

    @Test
    @DisplayName("Deve retornar status 'Service Unavailable' quando ocorrer erro de conexão com gateway de pagamentos.")
    public void validateCreditCardDonationRetryableExceptionTest() throws Exception {
        Mockito.when(paymentGatewayFeignClient.paymentCreditCard(Mockito.any(PaymentCreditCardDTO.class)))
                .thenThrow(RetryableException.class);

        String json = objectMapper.writeValueAsString(donationDTO);
        MockHttpServletRequestBuilder request = getValidateCreditCardDonationPostRequestWithToken(json, token);

        mvc.perform(request)
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    @DisplayName("Deve retornar status 'Unauthorized' quando encaminhar token gerado a partir de usuário inválido")
    public void invalidUserTokenTest() throws Exception {
        token = generateTokenWithInvalidUser();
        String json = objectMapper.writeValueAsString(donationDTO);
        MockHttpServletRequestBuilder request = getValidateCreditCardDonationPostRequestWithToken(json, token);

        mvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve passar pelo interceptador com sucesso quando o método http for OPTIONS")
    public void authInterceptorOptionsMethodTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .options(DONATION_CREDIT_CARD_VALIDATE_URI);

        mvc.perform(request)
                .andExpect(status().isOk());
    }
}
