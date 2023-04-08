package br.org.donations.donationsapi.controller;

import br.org.donations.donationsapi.dto.DonationCreditCardDTO;
import br.org.donations.donationsapi.dto.DonationResponse;
import br.org.donations.donationsapi.service.DonationCreditCardService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "DonationCreditCardController")
@RestController
@RequestMapping("credit-card")
public class DonationCreditCardController {

    @Autowired
    private DonationCreditCardService donationCreditCardService;

    @PostMapping("/validate")
    public DonationResponse validateCreditCardDonation(@RequestBody @Valid DonationCreditCardDTO donationCreditCardDTO){
        log.info("Iniciando processo de validação de doação por cartão de crédito.");

        DonationResponse donationResponse = donationCreditCardService.validateDonation(donationCreditCardDTO);

        log.info("Finalizado processo de validação de doação por cartão de crédito");
        return donationResponse;
    }


}
