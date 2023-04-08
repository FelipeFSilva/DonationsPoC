package br.org.donations.paymentgateway.unit.controller;

import br.org.donations.paymentgateway.controller.PaymentCreditCardController;
import br.org.donations.paymentgateway.dto.PaymentCreditCardDTO;
import br.org.donations.paymentgateway.enums.CardStatusEnum;
import br.org.donations.paymentgateway.service.PaymentCreditCardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static br.org.donations.paymentgateway.utils.TestCreditCardUtils.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PaymentCreditCardController.class)
class PaymentCreditCardControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PaymentCreditCardService paymentCreditCardService;

    private PaymentCreditCardDTO paymentCreditCardDTO;
    private String token;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        paymentCreditCardDTO = createValidPaymentCreditCardDTO();
        token = generateToken();
    }

    @Test
    @DisplayName("Deve validar um cartão de crédito com sucesso.")
    public void paymentCreditCardTest() throws Exception {
        Mockito.when(paymentCreditCardService.verifyPaymentCreditCard(paymentCreditCardDTO))
                .thenReturn(CardStatusEnum.VALID.getStatus());
        String json = getJson(paymentCreditCardDTO);
        MockHttpServletRequestBuilder request = getPostRequest(json, token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Válido"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando não estiverem preechidos corretamente os campos ao enviar os dados para validação")
    public void paymentCreditCardWithInvalidFieldTest() throws Exception {
        paymentCreditCardDTO.getCreditCard().setNumber("123");

        String json = getJson(paymentCreditCardDTO);
        MockHttpServletRequestBuilder request = getPostRequest(json, token);

        mvc.perform(request)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.title").value("Um ou mais campos estão inválidos."));
    }

    @Test
    @DisplayName("Deve lançar exceção não encaminhar token na request")
    public void paymentCreditCardWithoutTokenTest() throws Exception {

        String json = getJson(paymentCreditCardDTO);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(PAYMENT_CREDIT_CARD_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.title").value("Token de acesso não informado."));
    }

    @Test
    @DisplayName("Deve lançar exceção encaminhar token gerado com usuário inválido")
    public void paymentCreditCardWithInvalidUserTokenTest() throws Exception {
        token = generateTokenWithInvalidUser();
        String json = getJson(paymentCreditCardDTO);
        MockHttpServletRequestBuilder request = getPostRequest(json, token);

        mvc.perform(request)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title").value("Usuário inválido."));
    }

    @Test
    @DisplayName("Deve passar pelo interceptador com sucesso quando o método http for OPTIONS")
    public void authInterceptorOptionsMethodTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .options(PAYMENT_CREDIT_CARD_URI);

        mvc.perform(request)
                .andExpect(status().isOk());
    }
}