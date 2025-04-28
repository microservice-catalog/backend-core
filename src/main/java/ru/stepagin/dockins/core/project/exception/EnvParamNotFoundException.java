package ru.stepagin.dockins.core.project.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

public class EnvParamNotFoundException extends ProjectDomainException {
    public EnvParamNotFoundException(String message) {
        super(message, DomainErrorCodes.ENV_PARAM_NOT_FOUND);
    }
}