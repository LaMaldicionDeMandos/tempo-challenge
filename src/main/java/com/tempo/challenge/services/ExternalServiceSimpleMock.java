package com.tempo.challenge.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExternalServiceSimpleMock implements ExternalService {
    @Override
    public BigDecimal getFee() {
        return BigDecimal.TEN;
    }
}
