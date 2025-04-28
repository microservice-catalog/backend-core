package ru.stepagin.dockins.core.project.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

public class VersionNotFoundException extends ProjectDomainException {
    public VersionNotFoundException(String message) {
        super(message, DomainErrorCodes.VERSION_NOT_FOUND);
    }
}
