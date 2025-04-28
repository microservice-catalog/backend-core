package ru.stepagin.dockins.core.auth.exception;

import lombok.Getter;
import org.springframework.security.authentication.BadCredentialsException;

@Getter
public class AuthDomainException extends BadCredentialsException {
    private final int errorCode;

    public AuthDomainException(String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public AuthDomainException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
