package br.org.donations.donationsapi.utils;

import br.org.donations.donationsapi.exception.ValidationException;

public class DocumentNumberUtils {

    public static void validateDocumentNumber(String documentNumber) {

        if (documentNumberIsCPF(documentNumber)) {
            if (documentNumber.equals("00000000000")
                    || documentNumber.equals("11111111111")
                    || documentNumber.equals("22222222222")
                    || documentNumber.equals("33333333333")
                    || documentNumber.equals("44444444444")
                    || documentNumber.equals("55555555555")
                    || documentNumber.equals("66666666666")
                    || documentNumber.equals("77777777777")
                    || documentNumber.equals("88888888888")
                    || documentNumber.equals("99999999999")) {
                throw new ValidationException("O CPF informado é inválido.");
            }

        } else if (documentNumberIsCNPJ(documentNumber)) {
            if (documentNumber.equals("00000000000000")
                    || documentNumber.equals("11111111111111")
                    || documentNumber.equals("22222222222222")
                    || documentNumber.equals("33333333333333")
                    || documentNumber.equals("44444444444444")
                    || documentNumber.equals("55555555555555")
                    || documentNumber.equals("66666666666666")
                    || documentNumber.equals("77777777777777")
                    || documentNumber.equals("88888888888888")
                    || documentNumber.equals("99999999999999")) {
                throw new ValidationException("O CNPJ informado é inválido.");
            }
        } else {
            throw new ValidationException("O documento de doador informado possui tamanho inválido.");
        }
    }

    public static String hideCPFNumbers(String documentNumber) {
        return "***" + documentNumber.substring(3,9) + "**";
    }

    public static boolean documentNumberIsCPF(String documentNumber) {
        return documentNumber.length() == 11;
    }

    public static boolean documentNumberIsCNPJ(String documentNumber) {
        return documentNumber.length() == 14;
    }
}
