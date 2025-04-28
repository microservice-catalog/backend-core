package ru.stepagin.dockins.api.v1.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.stepagin.dockins.core.external.mail.EmailSendingException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<CustomErrorResponse> handleSendEmailException(EmailSendingException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.GATEWAY_TIMEOUT, ex.getMessage(), request, ex);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        StringBuilder messageBuilder = new StringBuilder("Не соблюдены ограничения данных: ");

        ex.getConstraintViolations().forEach(violation -> {
            messageBuilder
                    .append("[")
                    .append(violation.getPropertyPath())
                    .append(": ")
                    .append(violation.getMessage())
                    .append("] ");
        });

        String message = messageBuilder.toString().trim();
        CustomErrorResponse errorResponse = CustomErrorResponse.of(HttpStatus.BAD_REQUEST, message, request.getRequestURI());

        logError(errorResponse.getErrorId(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        StringBuilder messageBuilder = new StringBuilder("Ошибки валидации: ");

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            messageBuilder
                    .append("[")
                    .append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append("] ");
        });

        String message = messageBuilder.toString().trim();
        CustomErrorResponse errorResponse = CustomErrorResponse.of(HttpStatus.BAD_REQUEST, message, request.getRequestURI());

        logError(errorResponse.getErrorId(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request, ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleAll(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера", request, ex);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CustomErrorResponse> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка выполнения", request, ex);
    }

    private ResponseEntity<CustomErrorResponse> buildErrorResponse(HttpStatus status, String message, HttpServletRequest request, Exception ex) {
        String path = request.getRequestURI();
        CustomErrorResponse errorResponse = CustomErrorResponse.of(status, message, path);
        logError(errorResponse.getErrorId(), ex);
        return new ResponseEntity<>(errorResponse, status);
    }

    private void logError(String errorId, Exception ex) {
        log.error("Global Error ID [{}]:", errorId, ex);
    }
}
