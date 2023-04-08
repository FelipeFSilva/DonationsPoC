package br.org.donations.creditcardapi.controller;

import br.org.donations.creditcardapi.dto.DonationDTO;
import br.org.donations.creditcardapi.service.CreditCardService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "CreditCardController")
@RestController
@RequestMapping("creditcard")
public class CreditCardController {

    @Autowired
    private CreditCardService creditCardService;

    @PostMapping("send-donation")
    public ResponseEntity<String> sendCreditCardDonation(@RequestBody @Valid DonationDTO creditCardDonation){
        log.info("Requisição de doação recebida.");

        creditCardService.sendDonation(creditCardDonation);

        log.info("Envio de doação finalizado.");
        return ResponseEntity.ok().body("Doação enviada com sucesso!");
    }
}