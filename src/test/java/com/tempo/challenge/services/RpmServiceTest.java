package com.tempo.challenge.services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RpmServiceTest {
    @SpyBean
    private RpmService service;

    @SpyBean
    private RedisTemplate<String, Integer> redisTemplate;

    @Mock
    ValueOperations<String, Integer> op;

    private Long redisValue;

    private Boolean result;

    @Test
    @Disabled("Se deshabilita el test porque requiere de una conección con redis")
    void canProcessFirstTime() {
        givenAFirstTimeValueFromRedis();

        whenCanProcess();

        thenShouldExpireInOneMinute();
        thenResultShouldBeTrue();
    }

    @Test
    void canProcessSecondTime() {
        givenASecondTimeValueFromRedis();

        whenCanProcess();

        thenShouldNotCallExpire();
        thenResultShouldBeTrue();
    }

    @Test
    void canNotProcess() {
        givenALimitQuoteTimeValueFromRedis();

        whenCanProcess();

        thenShouldNotCallExpire();
        thenResultShouldBeFalse();
        thenResultCallRollback();
    }

    @Test
    void callRollback() {
        givenARedisWithValueOperation();
        whenCallRollback();

        thenShouldDecrementRedisValue();
    }

    private void givenAFirstTimeValueFromRedis() {
        redisValue = 1l;
        when(redisTemplate.opsForValue()).thenReturn(op);
        when(op.increment(anyString())).thenReturn(redisValue);
    }

    private void givenASecondTimeValueFromRedis() {
        redisValue = 2l;
        when(redisTemplate.opsForValue()).thenReturn(op);
        when(op.increment(anyString())).thenReturn(redisValue);
    }

    private void givenALimitQuoteTimeValueFromRedis() {
        redisValue = 4l;
        when(redisTemplate.opsForValue()).thenReturn(op);
        when(op.increment(anyString())).thenReturn(redisValue);
    }

    private void givenARedisWithValueOperation() {
        when(redisTemplate.opsForValue()).thenReturn(op);
    }

    private void whenCanProcess() {
        result = service.canProcess();
    }

    private void whenCallRollback() {
        service.rollback();
    }

    private void thenResultShouldBeTrue() {
        assertTrue(result);
    }

    private void thenResultShouldBeFalse() {
        assertFalse(result);
    }

    private void thenShouldExpireInOneMinute() {
        verify(redisTemplate).expire(anyString(), eq(1l), eq(TimeUnit.MINUTES));
    }

    private void thenShouldNotCallExpire() {
        verify(redisTemplate, never()).expire(anyString(), anyLong(), any());
    }

    private void thenResultCallRollback() {
        verify(service).rollback();
    }

    private void thenShouldDecrementRedisValue() {
        verify(op).decrement(anyString());
    }
}
