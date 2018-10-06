package br.com.tenorio.dm111projetofinal.exception;

public class InvalidDataException extends Exception {

    private String message;
    public InvalidDataException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}