package br.org.donations.authorizationapi.service;

import br.org.donations.authorizationapi.dto.CreditCard.CreditCardDTO;
import br.org.donations.authorizationapi.dto.CreditCard.DonationDTO;
import br.org.donations.authorizationapi.feignclients.CreditCardApiFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j(topic = "CreditCardService")
@Service
public class CreditCardService {

    @Autowired
    private CreditCardApiFeignClient creditCardApiFeignClient;

    public void sendDonation(DonationDTO creditCardDonation) {
        log.info("Iniciando envio de doação.");
        creditCardApiFeignClient.sendCreditCardDonation(creditCardDonation);
        log.info("Finalizando envio de doação.");
    }
}
