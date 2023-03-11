package com.tempo.challenge.services;

import com.tempo.challenge.errors.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static java.math.BigDecimal.TEN;
import static org.mockito.Mockito.*;

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

    @Test
    void externalServiceFailsFirstTime() {
        givenANumber1();
        givenANumber2();
        givenAServiceThatFails();

        assertThatThrownBy(() -> whenTryToCalculateResult()).isInstanceOf(BusinessException.class);

    }

    @Test
    void externalServiceFailsSecondTime() {
        givenANumber1();
        givenANumber2();
        givenAServiceThatFailsAfterSencondTime();

        whenTryToCalculateResult();
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

    private void givenAServiceThatFails() {
        when(externalService.getFee()).thenThrow(BusinessException.class);
    }

    private void givenAServiceThatFailsAfterSencondTime() {
        when(externalService.getFee())
                .thenReturn(TEN)
                .thenThrow(BusinessException.class);
    }

    private void whenTryToCalculateResult() {
        result = service.sumWithFee(number1, number2);
    }

    private void thenShouldReturnAValidResult() {
        assertEquals(BigDecimal.valueOf(22).intValueExact(), result.intValueExact());
    }
}
