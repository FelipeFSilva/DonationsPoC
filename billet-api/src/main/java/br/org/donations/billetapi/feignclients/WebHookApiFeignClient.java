package br.org.donations.billetapi.feignclients;

import br.org.donations.billetapi.dto.BilletDonationResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
@FeignClient(
        name = "webhook",
        contextId = "billet-api",
        url = "${app-config.services.webhook}",
        path = "/")
public interface WebHookApiFeignClient {
    @PostMapping
    BilletDonationResponseDTO sendDataWebHook(@RequestBody List<BilletDonationResponseDTO> listBilletDonationResponseDTO);
}
