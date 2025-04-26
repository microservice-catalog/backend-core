package ru.stepagin.dockins.domain.auth.exception;

import ru.stepagin.dockins.exception.DomainErrorCodes;

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