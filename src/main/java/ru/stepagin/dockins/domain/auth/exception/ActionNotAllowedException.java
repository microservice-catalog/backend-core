package ru.stepagin.dockins.domain.auth.exception;

import ru.stepagin.dockins.domain.DomainErrorCodes;

public class ActionNotAllowedException extends BadLoginDataException {
    public ActionNotAllowedException(String message) {
        super(message, DomainErrorCodes.ACCESS_DENIED);
    }

    public ActionNotAllowedException(String message, int code) {
        super(message, code);
    }

}
