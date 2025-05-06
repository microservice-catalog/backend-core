package ru.stepagin.dockins.core;

public class DomainErrorCodes {
    // Общие ошибки данных
    public static final int CONSTRAINT_VIOLATION = 1400;
    public static final int ENTITY_NOT_FOUND = 1404;

    // Ошибки обновления данных
    public static final int BAD_UPDATE_DATA = 3000;

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

    // Ошибки проектов
    public static final int PROJECT_NOT_FOUND = 5100; // Проект не найден
    public static final int PROJECT_ALREADY_EXISTS = 5101; // Проект уже существует
    public static final int PROJECT_ACCESS_DENIED = 5102; // Доступ к проекту запрещён
    public static final int PROJECT_NAME_IS_TOO_SHORT = 5103; // Имя пользователя слишком короткое
    public static final int PROJECT_NAME_IS_TOO_LONG = 5104; // Имя пользователя слишком длинное
    public static final int PROJECT_NAME_STARTS_WITH_BAD_SYMBOL = 5105;
    public static final int PROJECT_NAME_ENDS_WITH_BAD_SYMBOL = 5106;
    public static final int PROJECT_NAME_CONTAINS_BAD_SYMBOL = 5107;
    public static final int PROJECT_DESCRIPTION_IS_TOO_LONG = 5108;

    // Ошибки версий проектов
    public static final int VERSION_NOT_FOUND = 5200; // Версия не найдена
    public static final int VERSION_ALREADY_EXISTS = 5201; // Версия уже существует

    // Ошибки параметров окружения
    public static final int ENV_PARAM_NOT_FOUND = 5300; // Параметр окружения не найден
    public static final int ENV_PARAM_ALREADY_EXISTS = 5301; // Параметр окружения уже существует

    // Теги
    public static final int TAG_INVALID_FORMAT = 5400;

    // Внутренние ошибки проектов
    public static final int INTERNAL_PROJECT_ERROR = 5500; // Внутренняя ошибка проекта

    // Ошибки файлов
    public static final int BAD_FILE = 6000;
    public static final int FILE_NOT_FOUND = 6001;
    public static final int BAD_CONTENT_TYPE = 6002;
    public static final int TOO_BID_FILE_SIZE = 6003;

}
