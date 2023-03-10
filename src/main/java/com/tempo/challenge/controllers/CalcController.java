package com.tempo.challenge.controllers;

import com.tempo.challenge.dto.ResultDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController()
@RequestMapping(value = "calc", produces = MediaType.APPLICATION_JSON_VALUE)
public interface CalcController {

    @GetMapping(value = "plus", produces = "application/json")
    ResponseEntity<ResultDto> applySumAndAddFee(@RequestParam(name = "number1") BigDecimal number1,
                                                @RequestParam(name = "number2") BigDecimal number2);
}
