package ru.stepagin.dockins.core.user.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

public class UserDomainException extends RuntimeException {
    int errorCode;

    public UserDomainException(String message) {
        super(message);
        this.errorCode = DomainErrorCodes.BAD_UPDATE_DATA;
    }

    public UserDomainException(String message, int code) {
        super(message);
        this.errorCode = code;
    }

}
