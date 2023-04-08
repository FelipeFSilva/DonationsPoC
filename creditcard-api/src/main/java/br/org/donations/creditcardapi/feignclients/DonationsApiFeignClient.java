package br.org.donations.creditcardapi.feignclients;

import br.org.donations.creditcardapi.config.security.FeignClientInterceptor;
import br.org.donations.creditcardapi.dto.DonationDTO;
import br.org.donations.creditcardapi.dto.DonationToSaveDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(
        name = "donations-api",
        contextId = "donations-api",
        url = "${app-config.services.donations-api}",
        path = "/credit-card",
        configuration = FeignClientInterceptor.class)
public interface DonationsApiFeignClient {
    @PostMapping("/validate")
    DonationToSaveDTO validateCreditCardDonation(@RequestBody DonationDTO donationDTO);
}
