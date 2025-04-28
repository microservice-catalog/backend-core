package ru.stepagin.dockins.core.auth.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

public class ActionNotAllowedException extends BadLoginDataException {
    public ActionNotAllowedException(String message) {
        super(message, DomainErrorCodes.ACCESS_DENIED);
    }

    public ActionNotAllowedException(String message, int code) {
        super(message, code);
    }

}
