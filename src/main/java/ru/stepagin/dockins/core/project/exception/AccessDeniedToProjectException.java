package ru.stepagin.dockins.core.project.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

public class AccessDeniedToProjectException extends ProjectDomainException {
    public AccessDeniedToProjectException(String message) {
        super(message, DomainErrorCodes.PROJECT_ACCESS_DENIED);
    }
}