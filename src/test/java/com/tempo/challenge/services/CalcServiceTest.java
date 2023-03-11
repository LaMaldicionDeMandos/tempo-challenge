package com.tempo.challenge.services;

import com.tempo.challenge.ChallengeApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static java.math.BigDecimal.TEN;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {ChallengeApplication.class})
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
        assertEquals(BigDecimal.valueOf(22).intValueExact(), result.intValueExact());
    }
}
