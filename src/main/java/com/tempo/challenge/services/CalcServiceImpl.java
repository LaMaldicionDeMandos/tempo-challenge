package com.tempo.challenge.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CalcServiceImpl implements CalcService {
    @Override
    public BigDecimal sumWithFee(BigDecimal number1, BigDecimal number2) {
        return number1.add(number2).multiply(BigDecimal.valueOf(1.1));
    }
}
