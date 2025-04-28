package ru.stepagin.dockins.core.user.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

public class DomainEntityNotFoundException extends UserDomainException {
    public DomainEntityNotFoundException(String message) {
        super(message, DomainErrorCodes.ENTITY_NOT_FOUND);
    }

    public DomainEntityNotFoundException(String message, int code) {
        super(message, code);
    }
}
