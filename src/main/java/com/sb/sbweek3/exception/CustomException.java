package com.sb.sbweek3.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ExceptionErrorCode exceptionErrorCode;
    private final String userMessage;

    public CustomException(ExceptionErrorCode exceptionErrorCode) {
        this(exceptionErrorCode, exceptionErrorCode.getDetail());
    }

    public CustomException(ExceptionErrorCode exceptionErrorCode, String userMessage) {
        super(userMessage);
        this.exceptionErrorCode = exceptionErrorCode;
        this.userMessage = userMessage;
    }

    public CustomException(ExceptionErrorCode exceptionErrorCode, String userMessage, Throwable cause) {
        super(userMessage, cause);  // 여기서 cause는 원본 예외를 나타냄
        this.exceptionErrorCode = exceptionErrorCode;
        this.userMessage = userMessage;
    }
}
