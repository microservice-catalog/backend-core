package ru.stepagin.dockins.domain.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.stepagin.dockins.api.v1.auth.dto.ConfirmEmailDto;
import ru.stepagin.dockins.api.v1.auth.dto.LoginRequestDto;
import ru.stepagin.dockins.api.v1.auth.dto.RegisterRequestDto;
import ru.stepagin.dockins.domain.DomainErrorCodes;
import ru.stepagin.dockins.domain.auth.exception.BadRegistrationDataException;
import ru.stepagin.dockins.domain.auth.exception.EmailAlreadyExistsException;
import ru.stepagin.dockins.domain.auth.exception.UsernameAlreadyExistsException;
import ru.stepagin.dockins.domain.external.dadata.DadataValidationService;
import ru.stepagin.dockins.domain.external.mail.EmailConfirmationService;
import ru.stepagin.dockins.domain.user.repository.AccountRepository;
import ru.stepagin.dockins.security.JwtService;

@Service
@RequiredArgsConstructor
@Validated
public class AuthService {

    private final AccountRepository accountRepository;
    private final DadataValidationService dadataValidationService;
    private final JwtService jwtService;
    private final EmailConfirmationService emailConfirmationService;

    public void register(@Valid RegisterRequestDto requestDto) {
        validateRegistrationData(requestDto);
        jwtService.registerNewAccount(requestDto);
    }

    public void confirmEmail(@Valid ConfirmEmailDto confirmEmailDto) {
        emailConfirmationService.confirmEmail(confirmEmailDto.getEmail(), confirmEmailDto.getCode());
    }

    public void login(@Valid LoginRequestDto loginRequestDto, HttpServletResponse response) {
        jwtService.login(loginRequestDto, response);
    }

    public void refreshTokens(HttpServletResponse response) {
        jwtService.refreshTokens(response);
    }

    public void logout(HttpServletResponse response) {
        jwtService.clearAuthCookies(response);
    }


    private void validateRegistrationData(RegisterRequestDto requestDto) {
        validateUsername(requestDto.getUsername());
        validateEmail(requestDto.getEmail());
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
        if (accountRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException();
        }
        if (!username.matches("^[a-zA-Z].*$")) {
            throw new BadRegistrationDataException("Имя пользователя должно начинаться с латинской буквы.", DomainErrorCodes.USERNAME_STARTS_WITH_BAD_SYMBOL);
        }
        if (!username.matches("^[a-zA-Z][a-zA-Z0-9-]*$")) {
            throw new BadRegistrationDataException("Имя пользователя может содержать только латинские буквы, цифры и знак дефис.", DomainErrorCodes.USERNAME_CONTAINS_BAD_SYMBOL);
        }
    }

    private void validateEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BadRegistrationDataException("Неверный формат email.", DomainErrorCodes.BAD_EMAIL);
        }
        if (accountRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException();
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
}
