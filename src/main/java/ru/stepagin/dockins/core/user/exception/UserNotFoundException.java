package ru.stepagin.dockins.core.user.exception;

import ru.stepagin.dockins.core.common.exception.DomainEntityNotFoundException;

public class UserNotFoundException extends DomainEntityNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
