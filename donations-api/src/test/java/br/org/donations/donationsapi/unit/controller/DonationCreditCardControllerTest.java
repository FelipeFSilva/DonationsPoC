package br.org.donations.donationsapi.unit.controller;

import br.org.donations.donationsapi.controller.DonationCreditCardController;
import br.org.donations.donationsapi.dto.DonationCreditCardDTO;
import br.org.donations.donationsapi.dto.DonationResponse;
import br.org.donations.donationsapi.exception.ValidationException;
import br.org.donations.donationsapi.service.DonationCreditCardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RetryableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static br.org.donations.donationsapi.utils.TestUtils.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(controllers = DonationCreditCardController.class)
public class DonationCreditCardControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private DonationCreditCardService donationCreditCardService;
    private String token;

    @BeforeEach
    public void setUp() {
        token = generateToken();
    }

    @Test
    @DisplayName("Deve chamar o método de validação da camada Service e retornar doação com sucesso")
    public void validateCreditCardDonationTest() throws Exception {
        DonationCreditCardDTO donationDTO = createValidDonationDTO();
        DonationResponse donationResponse = createDonationResponse();
        Mockito.when(donationCreditCardService.validateDonation(donationDTO))
                .thenReturn(donationResponse);

        String json = objectMapper.writeValueAsString(donationDTO);
        MockHttpServletRequestBuilder request = getValidateCreditCardDonationPostRequestWithToken(json, token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.donor.name").value(donationResponse.getDonor().getName()))
                .andExpect(jsonPath("$.donor.type").value(donationResponse.getDonor().getType()))
                .andExpect(jsonPath("$.donor.documentNumber").value(donationResponse.getDonor().getDocumentNumber()))
                .andExpect(jsonPath("$.donor.email").value(donationResponse.getDonor().getEmail()))
                .andExpect(jsonPath("$.status").value(donationResponse.getStatus()))
                .andExpect(jsonPath("$.valuePaid").value(donationResponse.getValuePaid()));

    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao chamar o método de validação da camada Service")
    public void validateCreditCardDonationExceptionTest() throws Exception {
        DonationCreditCardDTO donationDTO = createValidDonationDTO();
        Mockito.doThrow(ValidationException.class)
                .when(donationCreditCardService).validateDonation(donationDTO);

        String json = objectMapper.writeValueAsString(donationDTO);
        MockHttpServletRequestBuilder request = getValidateCreditCardDonationPostRequestWithToken(json, token);

        mvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    @DisplayName("Deve lançar exceção quando não for possível estabelecer a conexão com o gateway de pagamentos.")
    public void validateCreditCardDonationRetryableExceptionTest() throws Exception {
        DonationCreditCardDTO donationDTO = createValidDonationDTO();
        Mockito.doThrow(RetryableException.class)
                .when(donationCreditCardService).validateDonation(donationDTO);

        String json = objectMapper.writeValueAsString(donationDTO);
        MockHttpServletRequestBuilder request = getValidateCreditCardDonationPostRequestWithToken(json, token);

        mvc.perform(request)
                .andExpect(status().isServiceUnavailable());
    }
}

