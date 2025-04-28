package ru.stepagin.dockins.core.auth.exception;

import ru.stepagin.dockins.core.DomainErrorCodes;

/**
 * Исключение выбрасывается, если токен недействителен или был подделан.
 * Возвращает статус 401 Unauthorized.
 */
public class TokenInvalidException extends AuthDomainException {
    public TokenInvalidException(String message) {
        super(message, DomainErrorCodes.TOKEN_INVALID);
    }

}
