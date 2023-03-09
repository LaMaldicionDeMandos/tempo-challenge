package com.tempo.challenge.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CalcControllerImpl implements CalcController {

    @Override
    public ResponseEntity<String> applySumAndAddFee(BigDecimal number1, BigDecimal number2) {
        return null;
    }
}
