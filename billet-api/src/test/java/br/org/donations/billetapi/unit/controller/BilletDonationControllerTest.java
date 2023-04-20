package br.org.donations.billetapi.unit.controller;

import br.org.donations.billetapi.controller.BilletDonationController;
import br.org.donations.billetapi.dto.BilletDonationDTO;
import br.org.donations.billetapi.service.BilletDonationService;
import br.org.donations.billetapi.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static br.org.donations.billetapi.utils.TestUtils.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(controllers = BilletDonationController.class)
public class BilletDonationControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BilletDonationService billetDonationService;

    private BilletDonationDTO billetDonationDTO;

    private String token;

    @BeforeEach
    public void setUp() throws Exception {
        token = TestUtils.generateToken();
        billetDonationDTO = createDonationInputDTO();
    }

    @Test
    @DisplayName("Deve enviar uma doação por boleto com sucesso")
    public void createDonationTest() throws Exception {
        String json = getJson(objectMapper, billetDonationDTO);
        MockHttpServletRequestBuilder request = getSendDonationPostRequestWithToken(json, token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Doação enviada com sucesso!"));
    }

    @Test
    @DisplayName("Deve recuperar todas as doações com sucesso")
    public void getAllDonationsTest() throws Exception {
        String json = getJson(objectMapper, billetDonationDTO);
        MockHttpServletRequestBuilder request = getSendDonationGetRequestWithToken(json, token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Dados serão postados no webhook assim que possível"));
    }
}
