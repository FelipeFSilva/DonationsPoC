package br.org.donations.paymentgateway.controller;

import br.org.donations.paymentgateway.dto.PaymentBilletDTO;
import br.org.donations.paymentgateway.service.PaymentBilletService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("billet")
@Slf4j(topic = "PaymentBilletController")
public class PaymentBilletController {

    @Autowired
    private PaymentBilletService paymentBilletService;

    @PostMapping("validate-payment")
    public String paymentCreditCard(@RequestBody @Valid PaymentBilletDTO paymentBilletDTO){
        log.info("Iniciando validação de pagamento por boleto.");
        String response = paymentBilletService.verifyPaymentBillet(paymentBilletDTO);
        log.info("Validação de pagamento por boleto finalizada.");
        return response;
    }
}
