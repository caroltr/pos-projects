package br.com.tenorio.dm111projetofinal.exception;

public class UserNotFoundException extends Exception {

    private String message;
    public UserNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}