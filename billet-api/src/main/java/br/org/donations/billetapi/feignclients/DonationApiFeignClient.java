package br.org.donations.billetapi.feignclients;

import br.org.donations.billetapi.config.security.FeignClientInterceptor;
import br.org.donations.billetapi.dto.BilletDonationDTO;
import br.org.donations.billetapi.dto.BilletDonationResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(
        name = "donations-api",
        contextId = "donations-api",
        url = "${app-config.services.donations-api}",
        path = "/billet",
        configuration = FeignClientInterceptor.class)
public interface DonationApiFeignClient {
    @PostMapping("/validate")
    BilletDonationResponseDTO validateBilletDonation(@RequestBody BilletDonationDTO donationDTO);
}
