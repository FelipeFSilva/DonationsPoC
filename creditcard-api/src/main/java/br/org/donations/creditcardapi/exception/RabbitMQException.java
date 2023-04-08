package br.org.donations.creditcardapi.exception;

public class RabbitMQException extends RuntimeException{

    public RabbitMQException(String messageError) {
        super(messageError);
    }

}
