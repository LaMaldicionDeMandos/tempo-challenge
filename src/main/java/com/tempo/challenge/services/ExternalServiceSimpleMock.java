package com.tempo.challenge.services;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@CacheConfig(cacheNames = "fee")
public class ExternalServiceSimpleMock implements ExternalService {
    @Override
    @Cacheable
    public BigDecimal getFee() {
        return BigDecimal.TEN;
    }
}
