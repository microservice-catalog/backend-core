package ru.stepagin.dockins.domain.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.stepagin.dockins.api.v1.auth.dto.ConfirmEmailDto;
import ru.stepagin.dockins.api.v1.auth.dto.LoginRequestDto;
import ru.stepagin.dockins.api.v1.auth.dto.RegisterRequestDto;
import ru.stepagin.dockins.domain.auth.repository.AccountRepository;
import ru.stepagin.dockins.external.dadata.DadataValidationService;
import ru.stepagin.dockins.external.mail.EmailConfirmationService;
import ru.stepagin.dockins.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final DadataValidationService dadataValidationService;
    private final JwtService jwtService;
    private final EmailConfirmationService emailConfirmationService;

    public void register(RegisterRequestDto requestDto) {
        dadataValidationService.validateEmail(requestDto.getEmail());
        jwtService.registerNewAccount(requestDto);
    }

    public void confirmEmail(ConfirmEmailDto confirmEmailDto) {
        emailConfirmationService.confirmEmail(confirmEmailDto.getEmail(), confirmEmailDto.getCode());
    }

    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        jwtService.login(loginRequestDto, response);
    }

    public void refreshTokens(HttpServletResponse response) {
        jwtService.refreshTokens(response);
    }

    public void logout(HttpServletResponse response) {
        jwtService.clearAuthCookies(response);
    }
}
