package ru.stepagin.dockins.core.auth.exception;

public class InvalidCredentialsException extends BadLoginDataException {
    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, int code) {
        super(message, code);
    }

}
