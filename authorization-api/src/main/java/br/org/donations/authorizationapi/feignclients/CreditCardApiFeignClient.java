package br.org.donations.authorizationapi.feignclients;

import br.org.donations.authorizationapi.dto.CreditCard.CreditCardDTO;
import br.org.donations.authorizationapi.dto.CreditCard.DonationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(
        name = "creditCard-api",
        contextId = "creditCard-api",
        url = "${app-config.services.credit-card-api}",
        path = "/creditcard")
public interface CreditCardApiFeignClient {

    @PostMapping("/send-donation")
    ResponseEntity<String> sendCreditCardDonation(@RequestBody DonationDTO donationDTO);
}
