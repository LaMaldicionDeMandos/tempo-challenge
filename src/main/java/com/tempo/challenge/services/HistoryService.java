package com.tempo.challenge.services;

import com.tempo.challenge.model.EndpointCall;

public interface HistoryService {
    void addToHistory(final EndpointCall endpointCall);
}
