package ru.stepagin.dockins.core.auth.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

public class AccountNotConfirmedException extends BadLoginDataException {
    public AccountNotConfirmedException(String message) {
        super(message, DomainErrorCodes.EMAIL_NOT_CONFIRMED);
    }

    public AccountNotConfirmedException(String message, int code) {
        super(message, code);
    }
}