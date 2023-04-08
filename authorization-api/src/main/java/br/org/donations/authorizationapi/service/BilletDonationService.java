package br.org.donations.authorizationapi.service;

import br.org.donations.authorizationapi.dto.billet.BilletDonationDTO;
import br.org.donations.authorizationapi.feignclients.BilletApiFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j(topic = "BilletDonationService")
@Service
public class BilletDonationService {

    @Autowired
    private BilletApiFeignClient billetApiFeignClient;

    public ResponseEntity<String> processBilletDonation(BilletDonationDTO billetDonationDTO){
        return billetApiFeignClient.sendBilletDonation(billetDonationDTO);
    }
}
