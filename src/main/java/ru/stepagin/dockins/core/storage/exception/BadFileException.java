package ru.stepagin.dockins.core.storage.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;
import ru.stepagin.dockins.core.common.exception.CommonDomainException;

public class BadFileException extends CommonDomainException {

    public BadFileException(String message) {
        super(message, DomainErrorCodes.BAD_FILE);
    }

    public BadFileException(String message, int code) {
        super(message, code);
    }

}
