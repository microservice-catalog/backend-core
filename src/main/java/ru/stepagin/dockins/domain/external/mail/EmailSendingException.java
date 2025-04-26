package ru.stepagin.dockins.domain.external.mail;

public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String message) {
        super(message);
    }
}
