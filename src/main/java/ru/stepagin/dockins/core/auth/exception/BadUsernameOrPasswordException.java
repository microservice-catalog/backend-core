package ru.stepagin.dockins.core.auth.exception;

public class BadUsernameOrPasswordException extends InvalidCredentialsException {
    public BadUsernameOrPasswordException() {
        super("Неверные имя пользователя или пароль");
    }
}
