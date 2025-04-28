package ru.stepagin.dockins.api.v1.project.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.stepagin.dockins.api.v1.common.CustomErrorResponse;
import ru.stepagin.dockins.core.project.exception.ProjectConstraintViolationException;
import ru.stepagin.dockins.core.project.exception.ProjectDomainException;

@Slf4j
@RestControllerAdvice
@Order(1)
public class ProjectExceptionHandler {

    @ExceptionHandler(ProjectConstraintViolationException.class)
    public ResponseEntity<CustomErrorResponse> handleProjectConstraintViolationException(ProjectConstraintViolationException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getErrorCode(), request, ex);
    }

    @ExceptionHandler(ProjectDomainException.class)
    public ResponseEntity<CustomErrorResponse> handleProjectDomainException(ProjectDomainException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getErrorCode(), request, ex);
    }

    private ResponseEntity<CustomErrorResponse> buildErrorResponse(HttpStatus status, String message, int code, HttpServletRequest request, Exception ex) {
        String path = request.getRequestURI();
        CustomErrorResponse errorResponse = CustomErrorResponse.of(status, message, path, code);
        logError(errorResponse.getErrorId(), ex);
        return new ResponseEntity<>(errorResponse, status);
    }

    private void logError(String errorId, Exception ex) {
        log.error("Project Error ID [{}]:", errorId, ex);
    }
}
