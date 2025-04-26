package ru.stepagin.dockins.domain.auth.exception;

/**
 * Исключение выбрасывается, если пользователь пытается войти в систему без подтверждения электронной почты.
 * Возвращает статус 403 Forbidden.
 */
public class AccountNotConfirmedException extends RuntimeException {
    public AccountNotConfirmedException(String message) {
        super(message);
    }
}