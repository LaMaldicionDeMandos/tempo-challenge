package com.tempo.challenge.services;

import java.math.BigDecimal;

public interface CalcService {
    BigDecimal sumWithFee(final BigDecimal number1, final BigDecimal number2);
}
