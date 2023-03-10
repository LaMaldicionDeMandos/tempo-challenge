package com.tempo.challenge.controllers;

import com.tempo.challenge.model.BusinessModelError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ControllerErrorHandler {

    private static final String REQUIRED_PARAM = "required_param";
    private static final String INVALID_PARAM = "invalid_param";

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BusinessModelError> handleMissingParams(final MissingServletRequestParameterException ex) {
        return createError(REQUIRED_PARAM, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BusinessModelError> handleInvalidParamType(final MethodArgumentTypeMismatchException ex) {
        return createError(INVALID_PARAM, ex.getMessage());
    }

    private ResponseEntity<BusinessModelError> createError(final String errorCode, final String errorMessage) {
        return ResponseEntity.badRequest()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(new BusinessModelError(errorCode, errorMessage));
    }
}
