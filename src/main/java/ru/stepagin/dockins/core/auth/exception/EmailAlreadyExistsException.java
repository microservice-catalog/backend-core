package ru.stepagin.dockins.core.auth.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

/**
 * Исключение выбрасывается, если email уже зарегистрирован в системе.
 * Возвращает статус 400 Bad Request.
 */
public class EmailAlreadyExistsException extends BadRegistrationDataException {
    public EmailAlreadyExistsException(String message) {
        super(message, DomainErrorCodes.EMAIL_ALREADY_EXISTS);
    }

    public EmailAlreadyExistsException() {
        super("Email уже зарегистрирован.", DomainErrorCodes.EMAIL_ALREADY_EXISTS);
    }

}