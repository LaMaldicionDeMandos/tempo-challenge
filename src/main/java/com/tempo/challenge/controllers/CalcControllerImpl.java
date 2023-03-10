package com.tempo.challenge.controllers;

import com.tempo.challenge.dto.ResultDto;
import com.tempo.challenge.services.CalcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CalcControllerImpl implements CalcController {

    private CalcService service;

    @Autowired
    public CalcControllerImpl(final CalcService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<ResultDto> applySumAndAddFee(BigDecimal number1, BigDecimal number2) {
        BigDecimal result = service.sumWithFee(number1, number2);
        return ResponseEntity.ok(new ResultDto(result));
    }
}
