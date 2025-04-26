package ru.stepagin.dockins.domain.auth.exception;

/**
 * Исключение выбрасывается, если токен доступа или обновления истек.
 * Возвращает статус 401 Unauthorized.
 */
public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }
}