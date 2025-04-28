package ru.stepagin.dockins.core.project.exception;

import ru.stepagin.dockins.core.common.exception.CommonDomainException;

public class ProjectDomainException extends CommonDomainException {

    public ProjectDomainException(String message, int errorCode) {
        super(message, errorCode);
    }

}
