package ru.stepagin.dockins.core.project.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

public class InternalProjectErrorException extends ProjectDomainException {
    public InternalProjectErrorException(String message) {
        super(message, DomainErrorCodes.INTERNAL_PROJECT_ERROR);
    }
}