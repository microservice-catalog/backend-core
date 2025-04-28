package ru.stepagin.dockins.core.common.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

public class BadUpdateDataException extends CommonDomainException {

    public BadUpdateDataException(String message) {
        super(message, DomainErrorCodes.BAD_UPDATE_DATA);
    }

    public BadUpdateDataException(String message, int code) {
        super(message, code);
    }

}
