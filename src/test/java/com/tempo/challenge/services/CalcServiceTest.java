package com.tempo.challenge.services;

import com.tempo.challenge.errors.BusinessException;
import com.tempo.challenge.errors.RejectByLimitException;
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

    @MockBean
    private RpmService rpmService;

    private BigDecimal number1;
    private BigDecimal number2;

    private BigDecimal result;

    @Test
    void correctSumWithFee() {
        givenANumber1();
        givenANumber2();
        givenARpmServiceThatPermitProcess();
        givenAServiceThatReturnAFeeInPercent();

        whenTryToCalculateResult();

        thenShouldReturnAValidResult();
        thenShouldNotRollbackRmpService();
    }

    @Test
    void externalServiceFailsFirstTime() {
        givenANumber1();
        givenANumber2();
        givenARpmServiceThatPermitProcess();
        givenAServiceThatFails();

        assertThatThrownBy(() -> whenTryToCalculateResult()).isInstanceOf(BusinessException.class);
        thenShouldRollbackRmpService();

    }

    @Test
    void externalServiceFailsSecondTime() {
        givenANumber1();
        givenANumber2();
        givenARpmServiceThatPermitProcess();
        givenAServiceThatFailsAfterSencondTime();

        whenTryToCalculateResult();
        whenTryToCalculateResult();

        thenShouldReturnAValidResult();
        thenShouldNotRollbackRmpService();
    }

    @Test
    void rejectedByRmp() {
        givenANumber1();
        givenANumber2();
        givenARpmServiceThatRejectProcess();

        assertThatThrownBy(() -> whenTryToCalculateResult()).isInstanceOf(RejectByLimitException.class);
        thenShouldNotRollbackRmpService();


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

    private void givenARpmServiceThatPermitProcess() {
        when(rpmService.canProcess()).thenReturn(true);
    }

    private void givenARpmServiceThatRejectProcess() {
        when(rpmService.canProcess()).thenReturn(false);
    }

    private void whenTryToCalculateResult() {
        result = service.sumWithFee(number1, number2);
    }

    private void thenShouldReturnAValidResult() {
        assertEquals(BigDecimal.valueOf(22).intValueExact(), result.intValueExact());
    }

    private void thenShouldRollbackRmpService() {
        verify(rpmService).rollback();
    }

    private void thenShouldNotRollbackRmpService() {
        verify(rpmService, never()).rollback();
    }
}
