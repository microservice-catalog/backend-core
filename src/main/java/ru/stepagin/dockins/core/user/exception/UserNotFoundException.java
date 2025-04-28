package ru.stepagin.dockins.core.user.exception;

public class UserNotFoundException extends DomainEntityNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
