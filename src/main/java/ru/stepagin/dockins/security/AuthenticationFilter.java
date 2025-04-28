package ru.stepagin.dockins.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
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
import ru.stepagin.dockins.core.auth.exception.TokenExpiredException;
import ru.stepagin.dockins.core.auth.exception.TokenInvalidException;
import ru.stepagin.dockins.core.user.entity.AccountEntity;
import ru.stepagin.dockins.core.user.repository.AccountRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final TokenGenerator tokenGenerator;
    private final AccountRepository accountRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = resolveAccessToken(request);

        if (accessToken != null) {
            try {
                Jws<Claims> claims = tokenGenerator.getParser().parseSignedClaims(accessToken);
                String userId = claims.getPayload().getSubject();

                AccountEntity account = accountRepository.findById(UUID.fromString(userId))
                        .orElseThrow(() -> new TokenInvalidException("Пользователь не найден"));

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        account, null, null
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                throw new TokenExpiredException("Access токен истёк");
            } catch (JwtException e) {
                throw new TokenInvalidException("Access токен недействителен " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
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
}
