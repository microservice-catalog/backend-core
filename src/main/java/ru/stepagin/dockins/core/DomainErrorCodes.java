package ru.stepagin.dockins.core;

public class DomainErrorCodes {
    // Ошибки токенов
    public static final int TOKEN_INVALID = 4000; // Неверный токен
    public static final int TOKEN_EXPIRED = 4001; // Токен истёк

    // Ошибки регистрации
    public static final int BAD_REGISTRATION_DATA = 4100; // Имя пользователя уже занято
    public static final int USERNAME_ALREADY_EXISTS = 4101; // Имя пользователя уже занято
    public static final int USERNAME_IS_TOO_SHORT = 4102; // Имя пользователя слишком короткое
    public static final int USERNAME_IS_TOO_LONG = 4103; // Имя пользователя слишком длинное
    public static final int EMAIL_ALREADY_EXISTS = 4104; // Email уже зарегистрирован
    public static final int PASSWORD_TOO_WEAK = 4105; // Слабый пароль
    public static final int USERNAME_STARTS_WITH_BAD_SYMBOL = 4106;
    public static final int USERNAME_ENDS_WITH_BAD_SYMBOL = 4107;
    public static final int USERNAME_CONTAINS_BAD_SYMBOL = 4108;
    public static final int BAD_EMAIL = 4109;
    public static final int FAKE_EMAIL = 4110;
    public static final int PASSWORD_IS_TOO_LONG = 4111;

    // Ошибки входа в аккаунт
    public static final int INVALID_CREDENTIALS = 4200; // Неверные учетные данные
    public static final int EMAIL_NOT_CONFIRMED = 4201; // Email не подтверждён

    // Ошибки доступа
    public static final int ACCESS_DENIED = 4300; // Доступ запрещен
    public static final int INSUFFICIENT_PRIVILEGES = 4301; // Недостаточно прав для выполнения действия
    public static final int RESOURCE_NOT_FOUND = 4302; // Ресурс не найден
    public static final int ACCOUNT_LOCKED = 4303; // Аккаунт заблокирован
}
