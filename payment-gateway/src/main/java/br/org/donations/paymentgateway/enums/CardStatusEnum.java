package br.org.donations.paymentgateway.enums;

public enum CardStatusEnum {

    INVALID_NUMBER("Número de cartão inválido"),
    NO_LIMIT("Sem limite"),
    FLAG_DECLINED("Bandeira recusada"),
    EXPIRED("Cartão expirado"),
    VALID("Válido");

    private final String status;

    CardStatusEnum(String value) {
        status = value;
    }

    public String getStatus(){
        return status;
    }
}
