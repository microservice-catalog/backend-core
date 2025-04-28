package ru.stepagin.dockins.core.project.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

public class EnvParamAlreadyExistsException extends ProjectDomainException {
    public EnvParamAlreadyExistsException(String message) {
        super(message, DomainErrorCodes.ENV_PARAM_ALREADY_EXISTS);
    }
}