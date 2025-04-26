package ru.stepagin.dockins.domain.auth.exception;

import ru.stepagin.dockins.domain.DomainErrorCodes;

/**
 * Исключение выбрасывается, если токен недействителен или был подделан.
 * Возвращает статус 401 Unauthorized.
 */
public class TokenInvalidException extends AuthDomainException {
    public TokenInvalidException(String message) {
        super(message, DomainErrorCodes.TOKEN_INVALID);
    }

}
