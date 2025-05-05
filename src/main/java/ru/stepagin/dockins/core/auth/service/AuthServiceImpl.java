package ru.stepagin.dockins.core.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.stepagin.dockins.api.v1.auth.dto.ConfirmEmailDto;
import ru.stepagin.dockins.api.v1.auth.dto.LoginRequestDto;
import ru.stepagin.dockins.api.v1.auth.dto.RegisterRequestDto;
import ru.stepagin.dockins.api.v1.auth.service.AuthDomainAuthServicePort;
import ru.stepagin.dockins.core.DomainErrorCodes;
import ru.stepagin.dockins.core.auth.AccountPrincipal;
import ru.stepagin.dockins.core.auth.exception.ActionNotAllowedException;
import ru.stepagin.dockins.core.auth.exception.BadRegistrationDataException;
import ru.stepagin.dockins.core.auth.exception.EmailAlreadyExistsException;
import ru.stepagin.dockins.core.auth.exception.UsernameAlreadyExistsException;
import ru.stepagin.dockins.core.auth.repository.AccountRepository;
import ru.stepagin.dockins.core.common.exception.BadUpdateDataException;
import ru.stepagin.dockins.core.external.dadata.DadataValidationService;
import ru.stepagin.dockins.core.external.mail.EmailConfirmationService;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.entity.ProjectVersionEntity;
import ru.stepagin.dockins.core.user.entity.AccountEntity;
import ru.stepagin.dockins.security.service.JwtService;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class AuthServiceImpl implements AuthDomainAuthServicePort {

    private final AccountRepository accountRepository;
    private final DadataValidationService dadataValidationService;
    private final JwtService jwtService;
    private final EmailConfirmationService emailConfirmationService;

    @Transactional
    @Override
    public void register(@Valid RegisterRequestDto requestDto) {
        // проверка правильности входных данных
        validateRegistrationData(requestDto);
        // находим пользователя с таким же username
        var existingAccountWithThisUsername = accountRepository.findByUsernameForRegistration(requestDto.getUsername())
                .orElse(null);
        if (existingAccountWithThisUsername != null) {
            // если пользователь с таким username уже подтвердил почту, то выбросить ошибку
            if (existingAccountWithThisUsername.getEmailConfirmed())
                throw new UsernameAlreadyExistsException();
            // иначе освобождаем username - удаляем аккаунт
            jwtService.deleteAccount(existingAccountWithThisUsername);
        }

        // проверяем существование подтверждённой почты в системе
        AccountEntity existingAccountWithThisEmail = accountRepository.findByEmailForRegistration(requestDto.getEmail())
                .orElse(null);
        if (existingAccountWithThisEmail != null) {

            // почта занята
            if (existingAccountWithThisEmail.getEmailConfirmed())
                throw new EmailAlreadyExistsException();

            // почта не подтверждена, проверяем надо ли её удалить
            if (existingAccountWithThisEmail.getUsername().equals(requestDto.getUsername())) {
                // email и username совпадают -> значит это тот же аккаунт, отправляем письмо подтверждения повторно
                emailConfirmationService.sendConfirmationEmail(existingAccountWithThisEmail);
                return;
            }
            // email и username не совпадают -> удаляем старые аккаунты
            jwtService.deleteAccount(existingAccountWithThisEmail);
        }

        // username не занят, почта не занята, создаём аккаунт
        AccountEntity newAccount = jwtService.registerNewAccount(requestDto);

        // отправляем письмо
        emailConfirmationService.sendConfirmationEmail(newAccount);
    }

    @Override
    public void confirmEmail(@Valid ConfirmEmailDto confirmEmailDto) {
        emailConfirmationService.confirmEmail(confirmEmailDto.getEmail(), confirmEmailDto.getCode());
    }

    @Override
    public void login(@Valid LoginRequestDto loginRequestDto, HttpServletResponse response) {
        jwtService.login(loginRequestDto, response);
    }

    @Override
    public void refreshTokens(HttpServletResponse response) {
        jwtService.refreshTokens(response);
    }

    @Override
    public void logout(HttpServletResponse response) {
        jwtService.clearAuthCookies(response);
    }


    private void validateRegistrationData(RegisterRequestDto requestDto) {
        validateEmail(requestDto.getEmail());
        validateUsername(requestDto.getUsername());
        if (!dadataValidationService.validateEmail(requestDto.getEmail())) {
            throw new BadRegistrationDataException("Fake email.", DomainErrorCodes.FAKE_EMAIL);
        }
        validatePassword(requestDto.getPassword());
    }

    private void validateUsername(String username) {
        if (username.length() > 30) {
            throw new BadRegistrationDataException("Имя пользователя должно быть от 5 до 30 символов.", DomainErrorCodes.USERNAME_IS_TOO_LONG);
        }
        if (username.length() < 5) {
            throw new BadRegistrationDataException("Имя пользователя должно быть от 5 до 30 символов.", DomainErrorCodes.USERNAME_IS_TOO_SHORT);
        }
        if (!username.matches("^[a-zA-Z].*$")) {
            throw new BadRegistrationDataException("Имя пользователя должно начинаться с латинской буквы.", DomainErrorCodes.USERNAME_STARTS_WITH_BAD_SYMBOL);
        }
        if (!username.matches("^.*[a-zA-Z0-9]$")) {
            throw new BadRegistrationDataException("Имя пользователя должно заканчиваться на латинскую букву или цифру.", DomainErrorCodes.USERNAME_ENDS_WITH_BAD_SYMBOL);
        }
        if (!username.matches("^[a-zA-Z][a-zA-Z0-9-]*[a-zA-Z0-9]$")) {
            throw new BadRegistrationDataException("Имя пользователя может содержать только латинские буквы, цифры и знак дефис.", DomainErrorCodes.USERNAME_CONTAINS_BAD_SYMBOL);
        }
    }

    private void validateEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BadRegistrationDataException("Неверный формат email.", DomainErrorCodes.BAD_EMAIL);
        }
    }

    private void validatePassword(String password) {
        if (password.length() < 8) {
            throw new BadRegistrationDataException("Пароль должен содержать не менее 8 символов.", DomainErrorCodes.PASSWORD_TOO_WEAK);
        }
        if (password.length() > 255) {
            throw new BadRegistrationDataException("Пароль должен содержать от 8 до 255 символов.", DomainErrorCodes.PASSWORD_IS_TOO_LONG);
        }
//        if (!password.matches(".*[A-Z].*")) {
//            throw new BadRegistrationDataException("Пароль должен содержать хотя бы одну заглавную букву.", DomainErrorCodes.PASSWORD_TOO_WEAK);
//        }
//        if (!password.matches(".*[0-9].*")) {
//            throw new BadRegistrationDataException("Пароль должен содержать хотя бы одну цифру.", DomainErrorCodes.PASSWORD_TOO_WEAK);
//        }
//        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
//            throw new BadRegistrationDataException("Пароль должен содержать хотя бы один спецсимвол.", DomainErrorCodes.PASSWORD_TOO_WEAK);
//        }
    }

    /**
     * Получаем текущего авторизованного пользователя.
     *
     * @return AccountEntity - текущий пользователь
     */
    public AccountEntity getCurrentUser() {
        String username = getCurrentUsername();
        return accountRepository.findByUsernameExactly(username)
                .orElseThrow(() -> new IllegalStateException("Текущий пользователь не найден"));
    }

    /**
     * Получаем текущего авторизованного пользователя.
     *
     * @return AccountEntity - текущий пользователь
     */
    @Nullable
    public AccountEntity getCurrentUserOrNull() {
        String username = getCurrentUsernameOrNull();
        if (username == null) {
            return null;
        }
        return accountRepository.findByUsernameExactly(username).orElse(null);
    }

    /**
     * Получаем имя текущего пользователя из SecurityContext.
     *
     * @return имя пользователя
     */
    private String getCurrentUsername() {
        AccountPrincipal userDetails = (AccountPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    /**
     * Получаем имя текущего пользователя из SecurityContext.
     *
     * @return имя пользователя
     */
    @Nullable
    private String getCurrentUsernameOrNull() {
        try {
            AccountPrincipal userDetails = (AccountPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userDetails.getUsername();
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * Обновление данных текущего пользователя.
     *
     * @param newUser AccountEntity - текущий пользователь с новыми данными
     */
    @Transactional
    public void updateCurrentUserData(AccountEntity newUser) {
        // Получаем текущего пользователя
        AccountEntity currentUser = getCurrentUser();

        // Проверяем, что нельзя обновить email
        if (!newUser.getEmail().equals(currentUser.getEmail())) {
            throw new BadUpdateDataException("Невозможно изменить email.");
        }

        // Проверяем, что имя пользователя не может быть изменено (если такая логика нужна)
        if (!newUser.getUsername().equals(currentUser.getUsername())) {
            throw new BadUpdateDataException("Невозможно изменить имя пользователя.");
        }

        // Обновляем поля
        currentUser.setFullName(newUser.getFullName());
        currentUser.setDescription(newUser.getDescription());
        currentUser.setAvatarUrl(newUser.getAvatarUrl());

        currentUser.goodFieldsOrThrow();
        accountRepository.save(currentUser);
    }

    public void belongToCurrentUserOrThrow(ProjectInfoEntity project) {
        AccountEntity currentUser = this.getCurrentUser();
        if (!project.getAuthorAccount().getId().equals(currentUser.getId())) {
            throw new ActionNotAllowedException(); // todo заменить на 404 Not Found
        }
    }

    public void belongToCurrentUserOrThrow(ProjectVersionEntity version) {
        belongToCurrentUserOrThrow(version.getProject());
    }
}
