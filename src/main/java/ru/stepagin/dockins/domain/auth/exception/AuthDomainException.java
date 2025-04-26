package ru.stepagin.dockins.domain.auth.exception;

import lombok.Getter;
import org.springframework.security.authentication.BadCredentialsException;

public class AuthDomainException extends BadCredentialsException {
    @Getter
    private final int errorCode;

    public AuthDomainException(String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public AuthDomainException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AuthDomainException(int errorCode) {
        super();
        this.errorCode = errorCode;
    }
}
