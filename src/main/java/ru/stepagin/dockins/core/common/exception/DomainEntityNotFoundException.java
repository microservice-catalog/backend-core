package ru.stepagin.dockins.core.common.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;
import ru.stepagin.dockins.core.user.exception.UserDomainException;

public class DomainEntityNotFoundException extends UserDomainException {
    public DomainEntityNotFoundException(String message) {
        super(message, DomainErrorCodes.ENTITY_NOT_FOUND);
    }

    public DomainEntityNotFoundException(String message, int code) {
        super(message, code);
    }
}
