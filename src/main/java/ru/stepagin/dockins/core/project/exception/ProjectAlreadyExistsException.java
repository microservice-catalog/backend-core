package ru.stepagin.dockins.core.project.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

public class ProjectAlreadyExistsException extends ProjectDomainException {
    public ProjectAlreadyExistsException(String message) {
        super(message, DomainErrorCodes.PROJECT_ALREADY_EXISTS);
    }
}