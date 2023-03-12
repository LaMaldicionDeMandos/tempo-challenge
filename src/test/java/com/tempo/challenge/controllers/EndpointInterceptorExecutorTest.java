package com.tempo.challenge.controllers;

import ch.qos.logback.core.util.TimeUtil;
import com.tempo.challenge.dto.ResultDto;
import com.tempo.challenge.errors.BusinessModelError;
import com.tempo.challenge.services.HistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.server.ServerHttpRequest;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class EndpointInterceptorExecutorTest {
    private static final String ERROR_CODE = "error_code";
    private static final String ERROR_MESSAGE = "error_message";

    @Autowired
    EndpointInterceptorExecutor interceptor;

    @MockBean
    HistoryService historyService;

    @Mock
    private ServerHttpRequest request;

    private Object body;

    @Test
    void saveRequestAndResponseData() throws URISyntaxException, InterruptedException {
        givenARequest();
        givenAResponseBody();

        whenTryHandleRequest();

        thenShouldSaveRequestAndResponse();
    }

    @Test
    void saveRequestAndResponseErrorData() throws URISyntaxException, InterruptedException {
        givenARequest();
        givenAResponseBodyError();

        whenTryHandleRequest();

        thenShouldSaveRequestAndResponseError();
    }

    private void givenARequest() throws URISyntaxException {
        when(request.getURI()).thenReturn(new URI("http://localhost/calc/plus?number1=5&number2=5"));
    }

    private void givenAResponseBody() {
        body = new ResultDto(BigDecimal.TEN);
    }

    private void givenAResponseBodyError() {
        body = new BusinessModelError(ERROR_CODE, ERROR_MESSAGE);
    }

    private void whenTryHandleRequest() {
        interceptor.postHandle(request, body);
    }

    private void thenShouldSaveRequestAndResponse() throws InterruptedException {
        String resultBody = "{\"result\":10}";
        TimeUnit.MILLISECONDS.sleep(100);
        verify(historyService).addToHistory("/calc/plus?number1=5&number2=5", resultBody);
    }

    private void thenShouldSaveRequestAndResponseError() throws InterruptedException {
        String resultBody = "{\"error_code\":\"error_code\",\"error_message\":\"error_message\"}";
        TimeUnit.MILLISECONDS.sleep(100);
        verify(historyService).addToHistory("/calc/plus?number1=5&number2=5", resultBody);
    }
}
