package com.tempo.challenge.errors;

public class RejectByLimitException extends BusinessException {
    public RejectByLimitException() {
        super(new BusinessModelError("quote_exceeded", "Limit quote exceeded."));
    }
}
