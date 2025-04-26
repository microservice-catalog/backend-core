package ru.stepagin.dockins.api.v1.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stepagin.dockins.api.v1.auth.dto.ConfirmEmailDto;
import ru.stepagin.dockins.api.v1.auth.dto.LoginRequestDto;
import ru.stepagin.dockins.api.v1.auth.dto.RegisterRequestDto;
import ru.stepagin.dockins.domain.auth.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequestDto requestDto) {
        authService.register(requestDto);
    }

    @PostMapping("/confirm")
    public void confirmEmail(@RequestBody ConfirmEmailDto confirmEmailDto) {
        authService.confirmEmail(confirmEmailDto);
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        authService.login(loginRequestDto, response);
    }

    @PostMapping("/refresh")
    public void refresh(HttpServletResponse response) {
        authService.refreshTokens(response);
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        authService.logout(response);
    }
}
