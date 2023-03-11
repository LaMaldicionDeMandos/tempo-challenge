package com.tempo.challenge.errors;

public class BusinessException extends RuntimeException {
    private final BusinessModelError error;

    public BusinessException(final BusinessModelError error) {
        super(error.getErrorMessage());
        this.error = error;
    }

    public BusinessModelError getError() {
        return error;
    }

}
