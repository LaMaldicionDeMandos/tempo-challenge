package com.tempo.challenge.controllers;

import com.tempo.challenge.model.EndpointCall;
import com.tempo.challenge.services.HistoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HistoryControllerImpl implements HistoryController {

    private final HistoryService historyService;

    public HistoryControllerImpl(final HistoryService historyService) {
        this.historyService = historyService;
    }

    @Override
    public ResponseEntity<Page<EndpointCall>> getHistory(Integer offset, Integer size) {
        return ResponseEntity.ok(historyService.getHistory(offset, size));
    }
}
