package com.tempo.challenge.controllers;

import com.tempo.challenge.errors.BusinessException;
import com.tempo.challenge.errors.BusinessModelError;
import com.tempo.challenge.errors.RejectByLimitException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BusinessModelError> handleBusinessError(final BusinessException ex) {
        return ResponseEntity.internalServerError()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(ex.getError());
    }

    @ExceptionHandler(RejectByLimitException.class)
    public ResponseEntity<BusinessModelError> handleBusinessError(final RejectByLimitException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(ex.getError());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BusinessModelError> handleBusinessError(final RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(new BusinessModelError("unknown_error", "Unknown Error."));
    }

    private ResponseEntity<BusinessModelError> createError(final String errorCode, final String errorMessage) {
        return ResponseEntity.badRequest()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(new BusinessModelError(errorCode, errorMessage));
    }
}
