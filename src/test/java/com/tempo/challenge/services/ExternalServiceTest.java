package com.tempo.challenge.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ExternalServiceTest {

    @SpyBean
    private ExternalService externalService;

    @Test
    void callCachedMethod() {
        whenCallMethodTwoTimes();
        thenOnlyOneTimeIsExecuted();
    }

    private void whenCallMethodTwoTimes() {
        externalService.getFee();
        externalService.getFee();
    }

    private void thenOnlyOneTimeIsExecuted() {
        verify(externalService, times(1)).getFee();
    }
}
