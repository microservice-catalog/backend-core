package ru.stepagin.dockins.core.common.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

public class DomainEntityNotFoundException extends CommonDomainException {
    public DomainEntityNotFoundException(String message) {
        super(message, DomainErrorCodes.ENTITY_NOT_FOUND);
    }

    public DomainEntityNotFoundException(String message, int code) {
        super(message, code);
    }
}
