package br.org.donations.donationsapi.service;

import br.org.donations.donationsapi.dto.BilletDonationResponse;
import br.org.donations.donationsapi.dto.DonationBilletDTO;
import br.org.donations.donationsapi.dto.DonationResponse;
import br.org.donations.donationsapi.feignclients.PaymentGatewayFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j(topic = "DonationBilletService")
@Service
public class DonationBilletService {

    @Autowired
    private PaymentGatewayFeignClient paymentGatewayFeignClient;

    public BilletDonationResponse validateDonationBillet(DonationBilletDTO donationBilletDTO){
        log.info("Iniciando processo de validação de doação por boleto.");
        var billetLink = paymentGatewayFeignClient.paymentBillet(donationBilletDTO);
        var response = BilletDonationResponse.donationBilletDTOToDonationResponse(donationBilletDTO, billetLink);
        log.info("Finalizando processo de validação de doação por boleto.");
        return response;
    }
}
