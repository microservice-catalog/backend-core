package ru.stepagin.dockins.domain.auth.exception;

public class InvalidCredentialsException extends BadLoginDataException {
    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, int code) {
        super(message, code);
    }

}
