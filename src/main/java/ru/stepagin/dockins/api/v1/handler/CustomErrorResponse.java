package ru.stepagin.dockins.api.v1.handler;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@RequiredArgsConstructor
public class CustomErrorResponse extends ProblemDetail {

    private final String timestamp;
    private final String errorId;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final Integer code; // наш собственный код ошибки, может быть null

    public CustomErrorResponse(HttpStatus httpStatus, String message, String path, Integer code) {
        super();
        this.timestamp = Instant.now().toString();
        this.errorId = UUID.randomUUID().toString();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
        this.path = path;
        this.code = code;
    }

    public static CustomErrorResponse of(HttpStatus status, String message, String path) {
        return new CustomErrorResponse(status, message, path, null);
    }

    public static CustomErrorResponse of(HttpStatus status, String message, String path, Integer code) {
        return new CustomErrorResponse(status, message, path, code);
    }

}
