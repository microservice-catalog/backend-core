package ru.stepagin.dockins.core.common.exception;

import lombok.Getter;

@Getter
public class CommonDomainException extends RuntimeException {
    private final int errorCode;

    public CommonDomainException(String message, int code) {
        super(message);
        this.errorCode = code;
    }

}
