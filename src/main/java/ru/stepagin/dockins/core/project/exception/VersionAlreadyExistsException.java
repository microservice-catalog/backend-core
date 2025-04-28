package ru.stepagin.dockins.core.project.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

public class VersionAlreadyExistsException extends ProjectDomainException {
    public VersionAlreadyExistsException(String message) {
        super(message, DomainErrorCodes.VERSION_ALREADY_EXISTS);
    }
}