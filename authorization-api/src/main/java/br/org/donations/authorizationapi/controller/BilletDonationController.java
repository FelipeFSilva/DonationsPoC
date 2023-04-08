package br.org.donations.authorizationapi.controller;

import br.org.donations.authorizationapi.dto.billet.BilletDonationDTO;
import br.org.donations.authorizationapi.service.BilletDonationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "BilletDonationController")
@RestController
@RequestMapping("billet")
public class BilletDonationController {

    @Autowired
    private BilletDonationService billetDonationService;

    @PostMapping("send-donation")
    public ResponseEntity<String> createBilletDonation(@RequestBody @Valid BilletDonationDTO billetDonationDTO){
        log.info("Envio de doação por boleto iniciada");
        ResponseEntity<String> responseEntity = billetDonationService.processBilletDonation(billetDonationDTO);
        log.info("Envio de doação finalizado.");
        return ResponseEntity.ok().body(responseEntity.getBody());
    }
}
