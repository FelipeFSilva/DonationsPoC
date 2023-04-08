package br.org.donations.donationsapi.controller;

import br.org.donations.donationsapi.dto.BilletDonationResponse;
import br.org.donations.donationsapi.dto.DonationBilletDTO;
import br.org.donations.donationsapi.dto.DonationResponse;
import br.org.donations.donationsapi.service.DonationBilletService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "DonationBilletController")
@RestController
@RequestMapping("billet")
public class DonationBilletController {

    @Autowired
    private DonationBilletService donationBilletService;

    @PostMapping("/validate")
    public BilletDonationResponse validateBilletDonation(@RequestBody @Valid DonationBilletDTO donationBilletDTO) {
        log.info("Iniciando processo de validação de doação por boleto.");
        var response = donationBilletService.validateDonationBillet(donationBilletDTO);
        log.info("Finalizado processo de validação de doação por boleto");
        return response;
    }
}
