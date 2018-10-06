package br.com.tenorio.dm111projetofinal.exception;

public class GCMRegistrationException extends Exception {

    private String message;
    public GCMRegistrationException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
