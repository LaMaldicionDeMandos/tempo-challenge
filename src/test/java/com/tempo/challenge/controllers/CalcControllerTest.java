package com.tempo.challenge.controllers;

import com.tempo.challenge.ChallengeApplication;
import com.tempo.challenge.errors.BusinessException;
import com.tempo.challenge.errors.BusinessModelError;
import com.tempo.challenge.services.CalcService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CalcController.class)
@ContextConfiguration(classes= ChallengeApplication.class)
public class CalcControllerTest extends AbstractControllerTest<CalcController>{
    private final static String ROOT = "/calc";

    @MockBean
    private CalcService service;

    @SpyBean
    private EndpointInterceptor interceptor;

    @MockBean
    private EndpointInterceptorExecutor interceptorExecutor;

    private String number1;
    private String number2;

    @Override
    protected String getRoot() {
        return ROOT;
    }

    private ResultActions result;

    @Test
    void sumWithNotRequiredParams() throws Exception {
        whenTryToSumWithNoRequiredParams();

        thenShouldBeA400BadRequestErrorWithMissingParamMessage();
    }

    @Test
    void sumWithNotRequiredParamNumber1() throws Exception {
        givenANumber2();
        whenTryToSumWithRequiredParamNumber2();

        thenShouldBeA400BadRequestErrorWithMissingParamMessage();
    }

    @Test
    void sumWithInvalidParamNumber1() throws Exception {
        givenANumber2();
        givenAInvalidNumber1();
        whenTryToSum();

        thenShouldBeA400BadRequestErrorWithInvalidParamMessage();
    }

    @Test
    void sumWithValidParams() throws Exception {
        givenANumber1();
        givenANumber2();
        givenAServiceThatReturnAValue();

        whenTryToSum();

        thenShouldCallService();
        thenShouldBeACorrectResult();
    }

    @Test
    void failsServiceWithBusinessException() throws Exception {
        givenANumber1();
        givenANumber2();
        givenAServiceThatThrowsABusinessException();

        whenTryToSum();

        thenShouldBeA500InternalServerError();
    }

    @Test
    void callEndpointInterceptor() throws Exception {
        givenANumber1();
        givenANumber2();
        givenAServiceThatReturnAValue();

        whenTryToSum();

        thenInterceptorShouldBeCalled();
    }


    private void givenANumber1() {
        number1 = "5";
    }

    private void givenANumber2() {
        number2 = "5";
    }

    private void givenAInvalidNumber1() {
        number1 = "a";
    }

    private void givenAServiceThatReturnAValue() {
        when(service.sumWithFee(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(11));
    }

    private void givenAServiceThatThrowsABusinessException() {
        when(service.sumWithFee(any(BigDecimal.class), any(BigDecimal.class))).thenThrow(
                new BusinessException(new BusinessModelError("internal_error", "Error with external service."))
        );
    }

    private void whenTryToSumWithNoRequiredParams() throws Exception {
        result = mvc.perform(get(ROOT + "/plus").header(CONTENT_TYPE, "application/json")).andDo(print());
    }

    private void whenTryToSumWithRequiredParamNumber2() throws Exception {
        result = mvc.perform(get(ROOT + "/plus").param("number2", number2)
                .header(CONTENT_TYPE, "application/json")).andDo(print());
    }

    private void whenTryToSum() throws Exception{
        result = mvc.perform(get(ROOT + "/plus")
                .param("number1", number1)
                .param("number2", number2)
                .header(CONTENT_TYPE, "application/json")).andDo(print());
    }

    private void thenShouldBeA400BadRequestErrorWithMissingParamMessage() throws Exception {
        result.andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.error_code", is("required_param")))
                .andExpect(jsonPath("$.error_message", is("Required request parameter 'number1' for method parameter type BigDecimal is not present")))
                .andReturn();
    }

    private void thenShouldBeA400BadRequestErrorWithInvalidParamMessage() throws Exception {
        result.andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.error_code", is("invalid_param")))
                .andExpect(jsonPath("$.error_message", is("Failed to convert value of type 'java.lang.String' to required type 'java.math.BigDecimal'; Character a is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.")))
                .andReturn();
    }

    private void thenShouldCallService() {
        verify(service).sumWithFee(BigDecimal.valueOf(Integer.valueOf(number1)), BigDecimal.valueOf(Integer.valueOf(number2)));
    }

    private void thenShouldBeACorrectResult() throws Exception {
        result.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.result", is(11)))
                .andReturn();
    }

    private void thenShouldBeA500InternalServerError() throws Exception {
        result.andExpect(status().isInternalServerError())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.error_code", is("internal_error")))
                .andExpect(jsonPath("$.error_message", is("Error with external service.")))
                .andReturn();
    }

    private void thenInterceptorShouldBeCalled() throws Exception {
        verify(interceptor).supports(any(), any());
        verify(interceptor).beforeBodyWrite(any(), any(), any(), any(), any(), any());
    }
}
