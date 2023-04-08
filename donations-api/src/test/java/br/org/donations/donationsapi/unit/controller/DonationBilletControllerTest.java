package br.org.donations.donationsapi.unit.controller;

import br.org.donations.donationsapi.controller.DonationBilletController;
import br.org.donations.donationsapi.dto.BilletDonationResponse;
import br.org.donations.donationsapi.dto.DonationBilletDTO;
import br.org.donations.donationsapi.dto.DonationResponse;
import br.org.donations.donationsapi.feignclients.PaymentGatewayFeignClient;
import br.org.donations.donationsapi.service.DonationBilletService;
import br.org.donations.donationsapi.service.DonationCreditCardService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@WebMvcTest(controllers = DonationBilletController.class)
public class DonationBilletControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private DonationBilletService donationBilletService;

    private String token;

    @BeforeEach
    public void setUp() {
        token = generateToken();
    }

    @Test
    @DisplayName("Deve chamar o método de validação da camada Service e retornar o objeto do tipo 'DonorResponse' no corpo da resposta")
    public void validateBilletDonationTest() throws Exception {
        DonationBilletDTO donationBilletDTO = createValidBilletDonationDTO();
        BilletDonationResponse donationResponse = createBilletDonationResponse();
        Mockito.when(donationBilletService.validateDonationBillet(donationBilletDTO))
                .thenReturn(donationResponse);

        String json = objectMapper.writeValueAsString(donationBilletDTO);
        MockHttpServletRequestBuilder request = getValidateBilletDonationPostRequest(json, token);
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.donor.name").value(donationResponse.getDonor().getName()))
                .andExpect(jsonPath("$.donor.type").value(donationResponse.getDonor().getType()))
                .andExpect(jsonPath("$.donor.documentNumber").value(donationResponse.getDonor().getDocumentNumber()))
                .andExpect(jsonPath("$.donor.email").value(donationResponse.getDonor().getEmail()))
                .andExpect(jsonPath("$.status").value(donationResponse.getStatus()))
                .andExpect(jsonPath("$.valuePaid").value(donationResponse.getValuePaid()))
                .andExpect(jsonPath("$.link").value(donationResponse.getLink()));
    }
}
