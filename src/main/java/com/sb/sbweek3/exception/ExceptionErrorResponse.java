package com.sb.sbweek3.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ExceptionErrorResponse {
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String code;
    private final String message;
    private final String userMessage;

    public static ResponseEntity<ExceptionErrorResponse> toResponseEntity(ExceptionErrorCode exceptionErrorCode) {
        return ResponseEntity
                .status(exceptionErrorCode.getHttpStatus())
                .body(ExceptionErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(exceptionErrorCode.getHttpStatus().value())
                        .error(exceptionErrorCode.getHttpStatus().name())
                        .code(exceptionErrorCode.name())
                        .message(exceptionErrorCode.getDetail())
                        .userMessage(exceptionErrorCode.getDetail())
                        .build()
                );
    }

    public static ResponseEntity<ExceptionErrorResponse> toResponseEntity(ExceptionErrorCode exceptionErrorCode, String userMessage) {
        return ResponseEntity
                .status(exceptionErrorCode.getHttpStatus())
                .body(ExceptionErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(exceptionErrorCode.getHttpStatus().value())
                        .error(exceptionErrorCode.getHttpStatus().name())
                        .code(exceptionErrorCode.name())
                        .message(exceptionErrorCode.getDetail())
                        .userMessage(userMessage)
                        .build()
                );
    }
}