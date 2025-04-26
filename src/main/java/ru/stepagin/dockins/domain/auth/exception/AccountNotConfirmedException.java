package ru.stepagin.dockins.domain.auth.exception;

import ru.stepagin.dockins.domain.DomainErrorCodes;

public class AccountNotConfirmedException extends BadLoginDataException {
    public AccountNotConfirmedException(String message) {
        super(message, DomainErrorCodes.EMAIL_NOT_CONFIRMED);
    }

    public AccountNotConfirmedException(String message, int code) {
        super(message, code);
    }
}