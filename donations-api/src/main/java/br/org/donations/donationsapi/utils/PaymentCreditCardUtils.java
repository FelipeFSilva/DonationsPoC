package br.org.donations.donationsapi.utils;

import br.org.donations.donationsapi.enums.CreditCardStatusEnum;

public class PaymentCreditCardUtils {

    public static boolean isValidPayment(String paymentStatus){
        return paymentStatus.equals(CreditCardStatusEnum.VALID.getStatus());
    }
}
