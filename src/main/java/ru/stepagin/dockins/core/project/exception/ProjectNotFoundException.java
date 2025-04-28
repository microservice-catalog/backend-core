package ru.stepagin.dockins.core.project.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

public class ProjectNotFoundException extends ProjectDomainException {
    public ProjectNotFoundException(String message) {
        super(message, DomainErrorCodes.PROJECT_NOT_FOUND);
    }
}