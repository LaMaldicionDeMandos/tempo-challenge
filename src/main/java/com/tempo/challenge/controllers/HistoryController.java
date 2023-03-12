package com.tempo.challenge.controllers;

import com.tempo.challenge.dto.ResultDto;
import com.tempo.challenge.model.EndpointCall;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "history", produces = MediaType.APPLICATION_JSON_VALUE)
public interface HistoryController {

    @GetMapping(value = "", produces = "application/json")
    ResponseEntity<Page<EndpointCall>> getHistory(@RequestParam(name = "offset", defaultValue = "0") Integer offset,
                                                  @RequestParam(name = "size", defaultValue = "20") Integer size);
}
