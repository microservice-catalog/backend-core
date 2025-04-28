package ru.stepagin.dockins.core.auth.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

/**
 * Исключение выбрасывается, если введены неверные учетные данные пользователя.
 * Возвращает статус 401 Unauthorized.
 */
public class BadLoginDataException extends AuthDomainException {
    public BadLoginDataException(String message) {
        super(message, DomainErrorCodes.INVALID_CREDENTIALS);
    }

    public BadLoginDataException(String message, int code) {
        super(message, code);
    }

}
