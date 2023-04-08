package br.org.donations.paymentgateway.unit.service;

import br.org.donations.paymentgateway.dto.PaymentCreditCardDTO;
import br.org.donations.paymentgateway.service.PaymentCreditCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static br.org.donations.paymentgateway.utils.TestCreditCardUtils.createValidPaymentCreditCardDTO;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class PaymentCreditCardServiceTest {

    @InjectMocks
    private PaymentCreditCardService paymentCreditCardService;

    private PaymentCreditCardDTO paymentCreditCardDTO;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        paymentCreditCardDTO = createValidPaymentCreditCardDTO();
    }

    @Test
    @DisplayName("Deve retornar 'Número de cartão inválido' quando o número do cartão de crédito não terminar com zero.")
    public void verifyCreditCardInvalidNumberTest() {

        paymentCreditCardDTO.getCreditCard().setNumber("5557892195065221");
        String status = paymentCreditCardService.verifyPaymentCreditCard(paymentCreditCardDTO);
        assertThat(status).isEqualTo("Número de cartão inválido");
    }


    @Test
    @DisplayName("Deve retornar 'Sem limite' quando o cartão de crédito não tiver limite disponível.")
    public void verifyCreditCardWithoutLimitTest() {

        paymentCreditCardDTO.getCreditCard().setNumber("5557892195065210");
        paymentCreditCardDTO.setValue(BigDecimal.valueOf(40.00));
        String status = paymentCreditCardService.verifyPaymentCreditCard(paymentCreditCardDTO);
        assertThat(status).isEqualTo("Sem limite");
    }

    @Test
    @DisplayName("Deve retornar 'Bandeira recusada' quando o número do cartão de crédito não iniciar com 55 ou 51.")
    public void verifyCreditCardInvalidFlagTest() {

        paymentCreditCardDTO.getCreditCard().setNumber("4457892195065220");
        String status = paymentCreditCardService.verifyPaymentCreditCard(paymentCreditCardDTO);
        assertThat(status).isEqualTo("Bandeira recusada");
    }


    @Test
    @DisplayName("Deve retornar 'Expirado' quando a data de validade do cartão estiver expirada.")
    public void verifyCreditCardExpiredTest() {

        paymentCreditCardDTO.getCreditCard().setValidity(LocalDate.of(2023, 01, 01));
        String status = paymentCreditCardService.verifyPaymentCreditCard(paymentCreditCardDTO);
        assertThat(status).isEqualTo("Cartão expirado");
    }

    @Test
    @DisplayName("Deve retornar 'Válido' quando o cartão informado for válido.")
    public void verifyCreditCardTest() {

        String status = paymentCreditCardService.verifyPaymentCreditCard(paymentCreditCardDTO);
        assertThat(status).isEqualTo("Válido");
    }
}