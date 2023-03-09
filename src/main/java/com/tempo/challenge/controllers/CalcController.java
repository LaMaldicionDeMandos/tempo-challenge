package com.tempo.challenge.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("calc")
public interface CalcController {

    @GetMapping(value = "plus", produces = "application/json")
    ResponseEntity<String> applySumAndAddFee(@RequestParam(name = "number1") BigDecimal number1,
                                             @RequestParam(name = "number2") BigDecimal number2);
}
