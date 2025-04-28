package ru.stepagin.dockins.core.auth.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

public class BadRegistrationDataException extends AuthDomainException {
    public BadRegistrationDataException(String message, int code) {
        super(message, code);
    }

    public BadRegistrationDataException(String message) {
        super(message, DomainErrorCodes.BAD_REGISTRATION_DATA);
    }

}
