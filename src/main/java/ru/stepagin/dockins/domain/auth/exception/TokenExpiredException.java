package ru.stepagin.dockins.domain.auth.exception;

import ru.stepagin.dockins.exception.DomainErrorCodes;

/**
 * Исключение выбрасывается, если токен доступа или обновления истек.
 * Возвращает статус 401 Unauthorized.
 */
public class TokenExpiredException extends AuthDomainException {
    public TokenExpiredException(String message) {
        super(message, DomainErrorCodes.TOKEN_EXPIRED);
    }

    public TokenExpiredException() {
        super(DomainErrorCodes.TOKEN_EXPIRED);
    }

}