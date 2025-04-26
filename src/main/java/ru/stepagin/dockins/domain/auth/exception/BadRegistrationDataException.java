package ru.stepagin.dockins.domain.auth.exception;

import ru.stepagin.dockins.exception.DomainErrorCodes;

public class BadRegistrationDataException extends AuthDomainException {
    public BadRegistrationDataException(String message, int code) {
        super(message, code);
    }

    public BadRegistrationDataException(String message) {
        super(message, DomainErrorCodes.BAD_REGISTRATION_DATA);
    }

}
