package br.org.donations.paymentgateway.service;

import br.org.donations.paymentgateway.dto.PaymentBilletDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "PaymentBilletService")
public class PaymentBilletService {
    public String verifyPaymentBillet(PaymentBilletDTO paymentBilletDTO) {
        return String.format("http://boleto.api.fake/%s", paymentBilletDTO.getDonor().getDocumentNumber());
    }
}
