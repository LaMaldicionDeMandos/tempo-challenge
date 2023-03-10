package com.tempo.challenge.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static java.math.BigDecimal.TEN;

@SpringBootTest
public class CalcServiceTest {

    @Autowired
    private CalcService service;

    @MockBean
    private ExternalService externalService;

    private BigDecimal number1;
    private BigDecimal number2;

    private BigDecimal result;

    @Test
    void correctSumWithFee() {
        givenANumber1();
        givenANumber2();
        givenAServiceThatReturnAFeeInPercent();

        whenTryToCalculateResult();

        thenShouldReturnAValidResult();
    }

    private void givenANumber1() {
        number1 = TEN;
    }

    private void givenANumber2() {
        number2 = TEN;
    }

    private void givenAServiceThatReturnAFeeInPercent() {
        when(externalService.getFee()).thenReturn(TEN);
    }

    private void whenTryToCalculateResult() {
        result = service.sumWithFee(number1, number2);
    }

    private void thenShouldReturnAValidResult() {
        Assertions.assertEquals(BigDecimal.valueOf(22).intValueExact(), result.intValueExact());
    }
}
