package com.tempo.challenge.services;

import com.tempo.challenge.errors.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static java.math.BigDecimal.ONE;

@Service
public class CalcServiceImpl implements CalcService {
    private Optional<BigDecimal> lastFee = Optional.empty();
    private static final BigDecimal NORMALIZER_FACTOR = BigDecimal.valueOf(0.01);
    private ExternalService externalService;

    @Autowired
    public CalcServiceImpl(final ExternalService externalService) {
        this.externalService = externalService;
    }
    @Override
    public BigDecimal sumWithFee(BigDecimal number1, BigDecimal number2) {
        BigDecimal fee = getFee().multiply(NORMALIZER_FACTOR).add(ONE);
        return number1.add(number2).multiply(fee);
    }


    private BigDecimal getFee() {
        try {
            lastFee = Optional.of(externalService.getFee());
        } catch (BusinessException ex) {
            return lastFee.orElseThrow(() -> ex);
        }
        return lastFee.get();
    }
}
