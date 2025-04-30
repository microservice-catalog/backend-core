package ru.stepagin.dockins.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.stepagin.dockins.core.auth.AccountPrincipal;
import ru.stepagin.dockins.core.auth.exception.TokenInvalidException;
import ru.stepagin.dockins.core.auth.repository.AccountRepository;
import ru.stepagin.dockins.core.user.entity.AccountEntity;
import ru.stepagin.dockins.security.service.JwtService;
import ru.stepagin.dockins.security.util.TokenGenerator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final TokenGenerator tokenGenerator;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        if (!request.getRequestURI().endsWith("/api/v1/auth/refresh"))
            handleAuthorizationInContext(request, response);

        filterChain.doFilter(request, response);
    }

    private void handleAuthorizationInContext(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = resolveAccessToken(request);
        if (accessToken == null && refreshTokenExists(request)) {
            refreshTokensOrClearCookies(request, response);
            return;
        }

        try {
            setAuthorizationInContext(request, accessToken);
        } catch (Exception e) {
            refreshTokensOrClearCookies(request, response);
        }

    }

    private void setAuthorizationInContext(HttpServletRequest request, String accessToken) {
        if (accessToken == null)
            throw new TokenInvalidException("Access token is null.");

        Jws<Claims> claims = tokenGenerator.getParser().parseSignedClaims(accessToken);
        UUID userId = UUID.fromString(claims.getPayload().getSubject());
        String username = claims.getPayload().get("username", String.class);

        // todo убрать поиск в БД для access токена
        AccountEntity account = accountRepository.findById(userId)
                .orElseThrow(() -> new TokenInvalidException("Пользователь не найден"));

        AccountPrincipal principal = new AccountPrincipal(account);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                principal, null, null
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private void refreshTokensOrClearCookies(HttpServletRequest request, HttpServletResponse response) {
        try {
            String newAccessToken = jwtService.refreshTokens(response);
            setAuthorizationInContext(request, newAccessToken);
        } catch (Exception ex) {
            jwtService.clearAuthCookies(response);
        }
    }

    private String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        if (request.getCookies() != null) {
            Optional<Cookie> cookie = Arrays.stream(request.getCookies())
                    .filter(c -> "dockins_access_token".equals(c.getName()))
                    .findFirst();
            return cookie.map(Cookie::getValue).orElse(null);
        }
        return null;
    }

    private boolean refreshTokenExists(HttpServletRequest request) {
        if (request.getCookies() != null) {
            Optional<Cookie> cookie = Arrays.stream(request.getCookies())
                    .filter(c -> "dockins_refresh_token".equals(c.getName()))
                    .findFirst();
            return cookie.isPresent();
        }
        return false;
    }
}
