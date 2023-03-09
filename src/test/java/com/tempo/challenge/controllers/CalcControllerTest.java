package com.tempo.challenge.controllers;

import org.junit.jupiter.api.Test;

public class CalcControllerTest extends AbstractControllerTest<CalcController>{
    private final static String ROOT = "/calc";
    @Override
    protected String getRoot() {
        return ROOT;
    }

    @Test
    void createWithNotRequiredParams() throws Exception {
        whenTryToSumWithNoRequiredParams();

        thenShouldBeA400BadRequestError();
    }
}
