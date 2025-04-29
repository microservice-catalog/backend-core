package ru.stepagin.dockins.api.v1.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.stepagin.dockins.api.common.CustomErrorResponse;
import ru.stepagin.dockins.core.auth.exception.*;

@Slf4j
@RestControllerAdvice
@Order(1)
public class AuthExceptionHandler {

    @ExceptionHandler({
            ActionNotAllowedException.class,
            AccountNotConfirmedException.class
    })
    public ResponseEntity<CustomErrorResponse> handleInvalidCredentials(AuthDomainException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), ex.getErrorCode(), request, ex);
    }

    @ExceptionHandler(BadRegistrationDataException.class)
    public ResponseEntity<CustomErrorResponse> handleAccountNotConfirmed(BadRegistrationDataException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getErrorCode(), request, ex);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<CustomErrorResponse> handleConstraintViolation(InvalidCredentialsException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), ex.getErrorCode(), request, ex);
    }

    @ExceptionHandler(AuthDomainException.class)
    public ResponseEntity<CustomErrorResponse> handleTokenExpired(AuthDomainException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), ex.getErrorCode(), request, ex);
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
