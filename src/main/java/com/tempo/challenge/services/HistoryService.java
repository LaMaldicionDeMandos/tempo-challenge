package com.tempo.challenge.services;

import com.tempo.challenge.model.EndpointCall;

public interface HistoryService {
    EndpointCall addToHistory(final String path, final String body);
}
