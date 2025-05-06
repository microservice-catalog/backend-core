package ru.stepagin.dockins.core.storage.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

public class LargeFileException extends BadFileException {

    public LargeFileException(String message) {
        super(message, DomainErrorCodes.TOO_BID_FILE_SIZE);
    }

    public LargeFileException(String message, int code) {
        super(message, code);
    }

}
