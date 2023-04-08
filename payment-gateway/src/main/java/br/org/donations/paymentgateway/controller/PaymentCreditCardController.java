package br.org.donations.paymentgateway.controller;

import br.org.donations.paymentgateway.dto.PaymentCreditCardDTO;
import br.org.donations.paymentgateway.service.PaymentCreditCardService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("credit-card")
@Slf4j(topic = "PaymentCreditCardController")
public class PaymentCreditCardController {

    @Autowired
    private PaymentCreditCardService paymentCreditCardService;

    @PostMapping("validate-payment")
    public String paymentCreditCard(@RequestBody @Valid PaymentCreditCardDTO paymentCreditCardDTO){
        log.info("Iniciando validação de pagamento por cartão de crédito.");
        String response = paymentCreditCardService.verifyPaymentCreditCard(paymentCreditCardDTO);
        log.info("Validação de pagamento por cartão de crédito finalizada.");
        return response;
    }
}