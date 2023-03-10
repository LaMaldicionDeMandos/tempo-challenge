package com.tempo.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ResultDto {
    private BigDecimal result;
}
