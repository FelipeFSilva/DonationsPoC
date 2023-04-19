package br.org.donations.creditcardapi.unit.controller;

import br.org.donations.creditcardapi.controller.CreditCardController;
import br.org.donations.creditcardapi.dto.DonationDTO;
import br.org.donations.creditcardapi.exception.RabbitMQException;
import br.org.donations.creditcardapi.service.CreditCardService;
import br.org.donations.creditcardapi.utils.TestUtils;
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

import static br.org.donations.creditcardapi.utils.TestUtils.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = CreditCardController.class)
public class CreditCardControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreditCardService creditCardService;

    private DonationDTO donationDTO;

    private String token;

    @BeforeEach
    public void setUp() throws Exception {
        token = TestUtils.generateToken();
        donationDTO = createDonationInputDTO();
    }

    @Test
    @DisplayName("Deve enviar uma doação por cartão de crédito com sucesso")
    public void createDonationTest() throws Exception {

        String json = getJson(donationDTO);

        MockHttpServletRequestBuilder request = getSendDonationPostRequestWithToken(json, token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Doação enviada com sucesso!"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorrer erro ao enviar uma doação por cartão de crédito")
    public void createDonationExceptionTest() throws Exception {

        Mockito.doThrow(RabbitMQException.class)
                .when(creditCardService).sendDonation(Mockito.any(DonationDTO.class));

        String json = getJson(donationDTO);
        MockHttpServletRequestBuilder request = getSendDonationPostRequestWithToken(json, token);

        mvc.perform(request)
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.title").value("Não foi possível enviar sua doação. Tente novamente em instantes."));
    }

    @Test
    @DisplayName("Deve lançar exceção quando não estiverem preechidos corretamente os campos ao enviar uma doação")
    public void createDonationWithInvalidFieldTest() throws Exception {
        donationDTO.getDonor().setName("");

        String json = getJson(donationDTO);
        MockHttpServletRequestBuilder request = getSendDonationPostRequestWithToken(json, token);

        mvc.perform(request)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.title").value("Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente."));
    }

    @Test
    @DisplayName("Deve lançar exceção quando existirem propriedades incorretas no JSON")
    public void createInvalidJSONTest() throws Exception {

        MockHttpServletRequestBuilder request = getSendDonationPostRequestWithToken("", token);
        mvc.perform(request)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.title").value("Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente."));
    }
}