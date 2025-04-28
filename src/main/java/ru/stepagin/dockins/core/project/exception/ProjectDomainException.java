package ru.stepagin.dockins.core.project.exception;

import lombok.Getter;

@Getter
public class ProjectDomainException extends RuntimeException {
    private final int errorCode;

    public ProjectDomainException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ProjectDomainException(String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
