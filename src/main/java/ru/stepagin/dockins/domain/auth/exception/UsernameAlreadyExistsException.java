package ru.stepagin.dockins.domain.auth.exception;

import ru.stepagin.dockins.domain.DomainErrorCodes;

/**
 * Исключение выбрасывается, если имя пользователя уже существует в системе.
 * Возвращает статус 400 Bad Request.
 */
public class UsernameAlreadyExistsException extends BadRegistrationDataException {
    public UsernameAlreadyExistsException(String message) {
        super(message, DomainErrorCodes.USERNAME_ALREADY_EXISTS);
    }

    public UsernameAlreadyExistsException() {
        super("Имя пользователя уже занято.", DomainErrorCodes.USERNAME_ALREADY_EXISTS);
    }

}