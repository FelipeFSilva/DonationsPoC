package br.org.donations.donationsapi.feignclients;

import br.org.donations.donationsapi.dto.DonationBilletDTO;
import br.org.donations.donationsapi.dto.PaymentCreditCardDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(
        name = "payment-gateway",
        contextId = "payment-gateway",
        url = "${app-config.services.payment-gateway}")
public interface PaymentGatewayFeignClient {

    @PostMapping("credit-card/validate-payment")
    String paymentCreditCard(@RequestBody @Valid PaymentCreditCardDTO paymentCreditCardDTO);

    @PostMapping("billet/validate-payment")
    String paymentBillet(@RequestBody @Valid DonationBilletDTO paymentCreditCardDTO);
}
