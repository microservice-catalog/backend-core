package ru.stepagin.dockins.core.storage.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

public class BadContentTypeException extends BadFileException {
    public BadContentTypeException(String message) {
        super(message, DomainErrorCodes.BAD_CONTENT_TYPE);
    }
}
