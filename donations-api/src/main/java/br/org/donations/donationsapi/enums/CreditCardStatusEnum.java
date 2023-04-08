package br.org.donations.donationsapi.enums;

public enum CreditCardStatusEnum {

    INVALID_NUMBER("Número de cartão inválido"),
    NO_LIMIT("Sem limite"),
    FLAG_DECLINED("Bandeira recusada"),
    EXPIRED("Cartão expirado"),
    VALID("Válido");

    private final String status;

    CreditCardStatusEnum(String value) {
        status = value;
    }

    public String getStatus(){
        return status;
    }
}
