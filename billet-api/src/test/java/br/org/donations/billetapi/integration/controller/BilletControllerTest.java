package br.org.donations.billetapi.integration.controller;

import br.org.donations.billetapi.dto.BilletDonationDTO;
import br.org.donations.billetapi.dto.BilletDonationResponseDTO;
import br.org.donations.billetapi.feignclients.DonationApiFeignClient;
import br.org.donations.billetapi.integration.IntegrationTest;
import br.org.donations.billetapi.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static br.org.donations.billetapi.utils.TestUtils.createValidDonationToSaveDTO;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class BilletControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private DonationApiFeignClient donationApiFeignClient;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    public void setUp() throws Exception {
        token = TestUtils.generateToken();
    }


    @Test
    @DisplayName("Deve enviar uma doação por boleto com sucesso.")
    public void sendBilletDonationWithSuccessTest() throws Exception{
        Mockito.when(donationApiFeignClient.validateBilletDonation(Mockito.any(BilletDonationDTO.class)))
                .thenReturn(createValidDonationToSaveDTO());

        BilletDonationDTO billetDonationDTO = TestUtils.createDonationInputDTO();
        String json = objectMapper.writeValueAsString(billetDonationDTO);

        MockHttpServletRequestBuilder request = TestUtils.getSendDonationPostRequestWithToken(json, token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Doação enviada com sucesso!"));
    }
}
