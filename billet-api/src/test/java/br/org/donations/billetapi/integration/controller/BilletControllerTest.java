package br.org.donations.billetapi.integration.controller;

import br.org.donations.billetapi.dto.BilletDonationDTO;
import br.org.donations.billetapi.dto.BilletDonationResponseDTO;
import br.org.donations.billetapi.feignclients.BilletApiFeignClient;
import br.org.donations.billetapi.integration.IntegrationTest;
import br.org.donations.billetapi.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class BilletControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BilletApiFeignClient billetApiFeignClient;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Deve enviar uma doação por boleto com sucesso.")
    public void sendBilletDonationWithSuccessTest() throws Exception{
        Mockito.when(billetApiFeignClient.validateBilletDonation(Mockito.any(BilletDonationDTO.class)))
                .thenReturn(Mockito.any(BilletDonationResponseDTO.class));

        BilletDonationDTO billetDonationDTO = TestUtils.createDonationInputDTO();
        String json = objectMapper.writeValueAsString(billetDonationDTO);

        MockHttpServletRequestBuilder request = TestUtils.sendBilletDonationPostRequest(json);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Doação enviada com sucesso!"));


    }
}
