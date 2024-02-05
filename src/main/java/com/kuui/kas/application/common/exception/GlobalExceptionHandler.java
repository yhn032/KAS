package com.kuui.kas.application.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleException(IllegalStateException illegalStateException) {
        ErrorResponse internalServerError = new ErrorResponse("Internal Server Error", illegalStateException.getMessage());
        return new ResponseEntity<>(internalServerError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
