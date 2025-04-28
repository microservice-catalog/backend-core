package ru.stepagin.dockins.core.user.exception;

import lombok.Getter;
import ru.stepagin.dockins.core.common.exception.CommonDomainException;

@Getter
public class UserDomainException extends CommonDomainException {

    public UserDomainException(String message, int code) {
        super(message, code);
    }

}
