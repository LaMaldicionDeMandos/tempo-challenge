package com.tempo.challenge.services;

import com.tempo.challenge.errors.BusinessException;
import com.tempo.challenge.errors.RejectByLimitException;
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
    private RpmService rpmService;

    @Autowired
    public CalcServiceImpl(final ExternalService externalService, final RpmService rpmService) {
        this.externalService = externalService;
        this.rpmService = rpmService;
    }
    @Override
    public BigDecimal sumWithFee(BigDecimal number1, BigDecimal number2) {
        if (!rpmService.canProcess()) throw new RejectByLimitException();
        BigDecimal fee = getFee().multiply(NORMALIZER_FACTOR).add(ONE);
        return number1.add(number2).multiply(fee);
    }


    private BigDecimal getFee() {
        try {
            lastFee = Optional.of(externalService.getFee());
        } catch (BusinessException ex) {
            return lastFee.orElseThrow(() -> {
                rpmService.rollback();
                return ex;
            });
        }
        return lastFee.get();
    }
}
