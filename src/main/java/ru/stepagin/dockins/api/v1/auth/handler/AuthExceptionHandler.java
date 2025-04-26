package ru.stepagin.dockins.api.v1.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.stepagin.dockins.domain.auth.exception.AccountNotConfirmedException;
import ru.stepagin.dockins.domain.auth.exception.InvalidCredentialsException;
import ru.stepagin.dockins.domain.auth.exception.TokenExpiredException;
import ru.stepagin.dockins.domain.auth.exception.TokenInvalidException;
import ru.stepagin.dockins.exception.CustomErrorResponse;
import ru.stepagin.dockins.exception.ErrorCodes;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<CustomErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), ErrorCodes.INVALID_CREDENTIALS, request, ex);
    }

    @ExceptionHandler(AccountNotConfirmedException.class)
    public ResponseEntity<CustomErrorResponse> handleAccountNotConfirmed(AccountNotConfirmedException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), ErrorCodes.EMAIL_NOT_CONFIRMED, request, ex);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<CustomErrorResponse> handleTokenExpired(TokenExpiredException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), ErrorCodes.TOKEN_EXPIRED, request, ex);
    }

    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<CustomErrorResponse> handleTokenInvalid(TokenInvalidException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), ErrorCodes.TOKEN_INVALID, request, ex);
    }

    private ResponseEntity<CustomErrorResponse> buildErrorResponse(HttpStatus status, String message, int code, HttpServletRequest request, Exception ex) {
        String path = request.getRequestURI();
        CustomErrorResponse errorResponse = CustomErrorResponse.of(status, message, path, code);
        logError(errorResponse.getErrorId(), ex);
        return new ResponseEntity<>(errorResponse, status);
    }

    private void logError(String errorId, Exception ex) {
        log.error("Auth Error ID [{}]:", errorId, ex);
    }
}
