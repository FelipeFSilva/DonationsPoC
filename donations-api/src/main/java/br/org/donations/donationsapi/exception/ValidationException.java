package br.org.donations.donationsapi.exception;

public class ValidationException extends RuntimeException{

    public ValidationException(String messageError) {
        super(messageError);
    }
}
