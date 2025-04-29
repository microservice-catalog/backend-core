package ru.stepagin.dockins.api.v1.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.api.v1.auth.dto.ConfirmEmailDto;
import ru.stepagin.dockins.api.v1.auth.dto.LoginRequestDto;
import ru.stepagin.dockins.api.v1.auth.dto.RegisterRequestDto;

public interface AuthDomainAuthServicePort {

    @Transactional
    void register(@Valid RegisterRequestDto requestDto);

    void confirmEmail(@Valid ConfirmEmailDto confirmEmailDto);

    void login(@Valid LoginRequestDto loginRequestDto, HttpServletResponse response);

    void refreshTokens(HttpServletResponse response);

    void logout(HttpServletResponse response);

}
