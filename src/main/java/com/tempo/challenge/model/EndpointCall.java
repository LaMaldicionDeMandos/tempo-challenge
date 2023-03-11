package com.tempo.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EndpointCall {
    private Integer id;
    private String path;
    private String result;
}
