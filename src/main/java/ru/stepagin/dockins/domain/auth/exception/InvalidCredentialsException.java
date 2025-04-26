package ru.stepagin.dockins.domain.auth.exception;

/**
 * Исключение выбрасывается, если введены неверные учетные данные пользователя.
 * Возвращает статус 401 Unauthorized.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
