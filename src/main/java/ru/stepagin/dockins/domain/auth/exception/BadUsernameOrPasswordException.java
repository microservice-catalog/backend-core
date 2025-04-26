package ru.stepagin.dockins.domain.auth.exception;

public class BadUsernameOrPasswordException extends InvalidCredentialsException {
    public BadUsernameOrPasswordException() {
        super("Неверные имя пользователя или пароль");
    }
}
