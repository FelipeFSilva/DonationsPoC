package br.org.donations.paymentgateway.unit.service;

import br.org.donations.paymentgateway.dto.PaymentBilletDTO;
import br.org.donations.paymentgateway.service.PaymentBilletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.org.donations.paymentgateway.utils.TestCreditCardUtils.createValidPaymentBilletDTO;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class PaymentBilletServiceTest {
    @InjectMocks
    private PaymentBilletService paymentBilletService;

    private PaymentBilletDTO paymentBilletDTO;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        paymentBilletDTO = createValidPaymentBilletDTO();
    }

    @Test
    @DisplayName("Deve retornar o link para o boleto.")
    public void verifyBilletLinkTest() {
        String linkBillet = paymentBilletService.verifyPaymentBillet(paymentBilletDTO);
        assertThat(linkBillet).isEqualTo("http://boleto.api.fake/43066629007");
    }
}
