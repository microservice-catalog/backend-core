package ru.stepagin.dockins.core.common.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;
import ru.stepagin.dockins.core.user.exception.UserDomainException;

public class BadUpdateDataException extends UserDomainException {

    public BadUpdateDataException(String message) {
        super(message, DomainErrorCodes.BAD_UPDATE_DATA);
    }

    public BadUpdateDataException(String message, int code) {
        super(message, code);
    }

}
