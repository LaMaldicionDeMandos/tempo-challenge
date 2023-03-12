package com.tempo.challenge.services;

import com.tempo.challenge.model.EndpointCall;
import org.springframework.data.domain.Page;

public interface HistoryService {
    EndpointCall addToHistory(final String path, final String body);

    Page<EndpointCall> getHistory(final int offset, final int size);
}
