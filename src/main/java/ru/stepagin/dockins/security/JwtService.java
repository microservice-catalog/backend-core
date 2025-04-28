package ru.stepagin.dockins.security;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.api.v1.auth.dto.LoginRequestDto;
import ru.stepagin.dockins.api.v1.auth.dto.RegisterRequestDto;
import ru.stepagin.dockins.core.auth.exception.AccountNotConfirmedException;
import ru.stepagin.dockins.core.auth.exception.BadUsernameOrPasswordException;
import ru.stepagin.dockins.core.auth.exception.TokenExpiredException;
import ru.stepagin.dockins.core.auth.exception.TokenInvalidException;
import ru.stepagin.dockins.core.user.entity.AccountEntity;
import ru.stepagin.dockins.core.user.repository.AccountRepository;
import ru.stepagin.dockins.util.RequestContextHolderUtil;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;
    private final CookieService cookieService;
    private final JwtParser jwtParser;

    @Transactional
    public AccountEntity registerNewAccount(@Valid RegisterRequestDto registerRequestDto) {
        AccountEntity account = AccountEntity.builder()
                .username(registerRequestDto.getUsername())
                .email(registerRequestDto.getEmail())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .emailConfirmed(false)
                .build();
        return accountRepository.save(account);
    }

    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        AccountEntity account = accountRepository.findByUsernameIgnoreCase(loginRequestDto.getUsername())
                .orElseThrow(BadUsernameOrPasswordException::new);

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), account.getPassword())) {
            throw new BadUsernameOrPasswordException();
        }

        if (!account.getEmailConfirmed()) {
            throw new AccountNotConfirmedException("Email пользователя не подтвержден");
        }

        String accessToken = tokenGenerator.generateAccessToken(account);
        String refreshToken = tokenGenerator.generateRefreshToken(account);

        cookieService.setAuthCookies(accessToken, refreshToken, response);
    }

    public void refreshTokens(HttpServletResponse response) {
        HttpServletRequest request = RequestContextHolderUtil.getRequest();
        String refreshToken = getCookieValue(request, "dockins_refresh_token")
                .orElseThrow(() -> new TokenInvalidException("Отсутствует refresh_token"));

        try {
            Jws<Claims> claims = jwtParser.parseSignedClaims(refreshToken);
            String userId = claims.getPayload().getSubject();

            AccountEntity account = accountRepository.findById(UUID.fromString(userId))
                    .orElseThrow(() -> new TokenInvalidException("Пользователь не найден"));

            String newAccessToken = tokenGenerator.generateAccessToken(account);
            String newRefreshToken = tokenGenerator.generateRefreshToken(account);

            cookieService.setAuthCookies(newAccessToken, newRefreshToken, response);
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("Refresh токен истёк");
        } catch (JwtException e) {
            throw new TokenInvalidException("Refresh токен недействителен");
        }
    }

    public void clearAuthCookies(HttpServletResponse response) {
        cookieService.clearAuthCookies(response);
    }

    private Optional<String> getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst()
                .map(Cookie::getValue);
    }
}