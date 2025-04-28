package ru.stepagin.dockins.core.project.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

public class ProjectConstraintViolationException extends ProjectDomainException {
    public ProjectConstraintViolationException(String message, int errorCode) {
        super(message, errorCode);
    }

    public ProjectConstraintViolationException(String message) {
        super(message, DomainErrorCodes.CONSTRAINT_VIOLATION);
    }

}
