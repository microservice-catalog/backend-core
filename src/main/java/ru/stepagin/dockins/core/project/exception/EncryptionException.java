package ru.stepagin.dockins.core.project.exception;

public class EncryptionException extends RuntimeException {
    public EncryptionException() {
        super();
    }

    public EncryptionException(String message) {
        super(message);
    }

    public EncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
