package ru.stepagin.dockins.domain.auth.exception;

/**
 * Исключение выбрасывается, если токен недействителен или был подделан.
 * Возвращает статус 401 Unauthorized.
 */
public class TokenInvalidException extends RuntimeException {
    public TokenInvalidException(String message) {
        super(message);
    }
}
