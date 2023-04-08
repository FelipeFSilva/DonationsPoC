package br.org.donations.authorizationapi.feignclients;

import br.org.donations.authorizationapi.dto.billet.BilletDonationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(
        name = "billet-api",
        contextId = "billet-api",
        url = "${app-config.services.billet-api}",
        path = "/billet")
public interface BilletApiFeignClient {
    @PostMapping("/send-donation")
    ResponseEntity<String> sendBilletDonation(@RequestBody BilletDonationDTO donationDTO);
}
