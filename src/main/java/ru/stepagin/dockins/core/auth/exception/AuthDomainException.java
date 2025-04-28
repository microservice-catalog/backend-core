package ru.stepagin.dockins.core.auth.exception;

import ru.stepagin.dockins.core.common.exception.CommonDomainException;

public class AuthDomainException extends CommonDomainException {

    public AuthDomainException(String message, int errorCode) {
        super(message, errorCode);
    }

}
