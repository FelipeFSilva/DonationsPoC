package br.org.donations.donationsapi.integration.controller;

import br.org.donations.donationsapi.DonationsApiApplication;
import br.org.donations.donationsapi.dto.DonationBilletDTO;
import br.org.donations.donationsapi.enums.DonationStatusEnum;
import br.org.donations.donationsapi.enums.TypePersonEnum;
import br.org.donations.donationsapi.feignclients.PaymentGatewayFeignClient;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static br.org.donations.donationsapi.utils.TestUtils.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@ImportAutoConfiguration
@SpringBootTest(classes = DonationsApiApplication.class)
class DonationBilletControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private PaymentGatewayFeignClient paymentGatewayFeignClient;
    @Autowired
    private ObjectMapper objectMapper;

    private DonationBilletDTO donationBilletDTO;
    private String token;

    @BeforeEach
    public void setUp() {
        donationBilletDTO = createValidBilletDonationDTO();
        token = generateToken();
    }

    @Test
    @DisplayName("Deve retornar uma doação de boleto.")
    public void validateBilletDonationIntegrationTest() throws Exception {
        Mockito.when(paymentGatewayFeignClient.paymentBillet(Mockito.any(DonationBilletDTO.class)))
                .thenReturn("http://boleto.api.fake/00011122233");

        String json = objectMapper.writeValueAsString(donationBilletDTO);
        MockHttpServletRequestBuilder request = getValidateBilletDonationPostRequest(json, token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.donor.name").value(donationBilletDTO.getDonor().getName()))
                .andExpect(jsonPath("$.donor.type").value(TypePersonEnum.PF.toString()))
                .andExpect(jsonPath("$.donor.email").value(donationBilletDTO.getDonor().getEmail()))
                .andExpect(jsonPath("$.status").value(DonationStatusEnum.PENDING.toString()))
                .andExpect(jsonPath("$.valuePaid").value(donationBilletDTO.getValue()))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.link").value("http://boleto.api.fake/00011122233"));
    }

}
