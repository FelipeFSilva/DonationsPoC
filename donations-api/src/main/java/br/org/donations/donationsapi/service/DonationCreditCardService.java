package br.org.donations.donationsapi.service;

import br.org.donations.donationsapi.dto.DonationCreditCardDTO;
import br.org.donations.donationsapi.dto.DonationResponse;
import br.org.donations.donationsapi.dto.PaymentCreditCardDTO;
import br.org.donations.donationsapi.feignclients.PaymentGatewayFeignClient;
import br.org.donations.donationsapi.utils.DocumentNumberUtils;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j(topic = "DonationCreditCardService")
@Service
public class DonationCreditCardService {

    @Autowired
    private PaymentGatewayFeignClient paymentGatewayFeignClient;

    public DonationResponse validateDonation(DonationCreditCardDTO donationCreditCardDTO){
        log.info("Validação iniciada.");

        DocumentNumberUtils.validateDocumentNumber(donationCreditCardDTO.getDonor().getDocumentNumber());
        String paymentStatus;
        try {
            PaymentCreditCardDTO paymentToValidate = PaymentCreditCardDTO.donationCreditCardDTOToPaymentCreditCardDTO(donationCreditCardDTO);
            paymentStatus = paymentGatewayFeignClient.paymentCreditCard(paymentToValidate);
        }catch (RetryableException ex){
            log.error("Não foi possível estabelecer conexão com o gateway de pagamento: " + ex);
            throw ex;
        }
        DonationResponse donationResponse = DonationResponse.donationCreditCardDTOToDonationResponse(donationCreditCardDTO, paymentStatus);

        log.info("Validação finalizada.");
        return donationResponse;
    }

}
