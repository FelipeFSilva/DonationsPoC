package br.org.donations.paymentgateway.unit.controller;

import br.org.donations.paymentgateway.controller.PaymentBilletController;
import br.org.donations.paymentgateway.dto.PaymentBilletDTO;
import br.org.donations.paymentgateway.service.PaymentBilletService;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static br.org.donations.paymentgateway.utils.TestCreditCardUtils.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PaymentBilletController.class)
class PaymentBilletControllerTest {
    @Autowired
    MockMvc mvc;
    @MockBean
    private PaymentBilletService paymentBilletService;

    private PaymentBilletDTO paymentBilletDTO;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        paymentBilletDTO = createValidPaymentBilletDTO();
    }

    @Test
    @DisplayName("Deve retornar um link de boleto com sucesso.")
    void paymentBilletTest() throws Exception {
        Mockito.when(paymentBilletService.verifyPaymentBillet(paymentBilletDTO))
                .thenReturn(String.format("http://billet.api.fake/%s", paymentBilletDTO.getDonor().getDocumentNumber()));
        String json = getBilletJson(paymentBilletDTO);
        MockHttpServletRequestBuilder request = getBilletPostRequest(json);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("http://billet.api.fake/43066629007"));
    }
}
