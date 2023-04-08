package br.org.donations.paymentgateway.service;

import br.org.donations.paymentgateway.dto.PaymentCreditCardDTO;
import br.org.donations.paymentgateway.enums.CardStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static br.org.donations.paymentgateway.utils.CreditCardUtils.*;

@Service
@Slf4j(topic = "PaymentCreditCardService")
public class PaymentCreditCardService {

    public String verifyPaymentCreditCard(PaymentCreditCardDTO paymentCreditCardDTO) {
        log.info("Verificando cartão de crédito.");
        String creditCardNumber = paymentCreditCardDTO.getCreditCard().getNumber();

        if(!cardNumberIsValid(creditCardNumber))
            return CardStatusEnum.INVALID_NUMBER.getStatus();

        if(hasNoLimit(creditCardNumber, paymentCreditCardDTO.getValue()))
            return CardStatusEnum.NO_LIMIT.getStatus();

        if(cardFlagDeclined(creditCardNumber))
            return CardStatusEnum.FLAG_DECLINED.getStatus();

        if(validityCardExpired(paymentCreditCardDTO.getCreditCard().getValidity()))
            return CardStatusEnum.EXPIRED.getStatus();

        return CardStatusEnum.VALID.getStatus();
    }
}