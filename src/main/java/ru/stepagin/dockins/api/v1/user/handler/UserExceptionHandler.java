package ru.stepagin.dockins.api.v1.user.handler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.stepagin.dockins.api.common.CustomErrorResponse;
import ru.stepagin.dockins.core.user.exception.UserDomainException;

@Slf4j
@RestControllerAdvice
@Order(1)
public class UserExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleSendEmailException(EntityNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, ex);
    }

    @ExceptionHandler(UserDomainException.class)
    public ResponseEntity<CustomErrorResponse> handleSendEmailException(UserDomainException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request, ex);
    }

    private ResponseEntity<CustomErrorResponse> buildErrorResponse(HttpStatus status, String message, HttpServletRequest request, Exception ex) {
        String path = request.getRequestURI();
        CustomErrorResponse errorResponse = CustomErrorResponse.of(status, message, path);
        logError(errorResponse.getErrorId(), ex);
        return new ResponseEntity<>(errorResponse, status);
    }

    private void logError(String errorId, Exception ex) {
        log.error("User handler Error ID [{}]:", errorId, ex);
    }
}
