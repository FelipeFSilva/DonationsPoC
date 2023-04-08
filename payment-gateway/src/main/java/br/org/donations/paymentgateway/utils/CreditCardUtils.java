package br.org.donations.paymentgateway.utils;


import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j(topic = "CreditCardUtils")
public class CreditCardUtils {

    public static boolean cardNumberIsValid(String creditCardNumber){
        log.info("Validando número do cartão de crédito.");
        return creditCardNumber.endsWith("0");
    }

    public static boolean validityCardExpired(LocalDate validity){
        log.info("Validando expiração do cartão de crédito.");
        return validity.isBefore(LocalDate.now());
    }

    public static boolean cardFlagDeclined(String creditCardNumber){
        log.info("Validando bandeira do cartão de crédito.");
        return !creditCardNumber.startsWith("55");
    }

    public static boolean hasNoLimit(String creditCardNumber, BigDecimal valuePaid){
        log.info("Validando limite do cartão de crédito.");
        return creditCardNumber.endsWith("10")
                && (valuePaid.doubleValue() > BigDecimal.valueOf(10.00).doubleValue());
    }
}
