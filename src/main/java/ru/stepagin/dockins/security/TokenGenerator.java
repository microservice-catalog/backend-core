package ru.stepagin.dockins.security;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.stepagin.dockins.core.common.util.SecretStorageUtil;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class TokenGenerator {
    private static final long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000; // 15 минут
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 дней
    @Getter
    private final SecretKey key;

    public TokenGenerator(SecretStorageUtil secretStorageUtil) {
        this.key = Keys.hmacShaKeyFor(secretStorageUtil.getJwtSignatureKey());
    }

    public String generateAccessToken(AccountEntity account) {
        return Jwts.builder()
                .subject(account.getId().toString())
                .claim("username", account.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(AccountEntity account) {
        return Jwts.builder()
                .subject(account.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .signWith(key)
                .compact();
    }

    public JwtParser getParser() {
        return Jwts.parser()
                .verifyWith(key)
                .build();
    }

    @Bean
    public JwtParser jwtParser() {
        return this.getParser();
    }
}

