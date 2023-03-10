package com.tempo.challenge.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@WebMvcTest(CalcController.class)
public class CalcControllerTest extends AbstractControllerTest<CalcController>{
    private final static String ROOT = "/calc";
    @Override
    protected String getRoot() {
        return ROOT;
    }

    private ResultActions result;

    @Test
    void createWithNotRequiredParams() throws Exception {
        whenTryToSumWithNoRequiredParams();

        thenShouldBeA400BadRequestError();
    }

    private void whenTryToSumWithNoRequiredParams() throws Exception {
        result = mvc.perform(get(ROOT + "/plus").header(CONTENT_TYPE, "application/json")).andDo(print());
    }

    private void thenShouldBeA400BadRequestError() throws Exception {
        result.andExpect(status().isBadRequest())
                .andReturn();
    }
}
