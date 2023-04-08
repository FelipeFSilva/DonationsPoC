package br.org.donations.billetapi.controller;

import br.org.donations.billetapi.dto.BilletDonationDTO;
import br.org.donations.billetapi.service.BilletDonationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "BilletDonationService")
@RestController
@RequestMapping("billet")
public class BilletDonationController {

    @Autowired
    private BilletDonationService billetDonationService;

    @PostMapping("send-donation")
    public ResponseEntity<String> createBilletDonation(@RequestBody @Valid BilletDonationDTO billetDonationDTO){
        log.info("Envio de doação por boleto iniciada");
        billetDonationService.processBilletDonation(billetDonationDTO);
        log.info("Envio de doação finalizado.");
        return ResponseEntity.ok().body("Doação enviada com sucesso!");
    }
}
