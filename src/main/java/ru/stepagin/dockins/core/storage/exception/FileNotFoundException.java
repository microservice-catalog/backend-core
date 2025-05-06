package ru.stepagin.dockins.core.storage.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;
import ru.stepagin.dockins.core.common.exception.CommonDomainException;

public class FileNotFoundException extends CommonDomainException {
    public FileNotFoundException(String id) {
        super("File with id='" + id + "' not found", DomainErrorCodes.FILE_NOT_FOUND);
    }
}
