package com.tempo.challenge.services;

import com.tempo.challenge.model.EndpointCall;
import com.tempo.challenge.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository repo;

    @Autowired
    public HistoryServiceImpl(final HistoryRepository repo) {
        this.repo = repo;
    }
    @Override
    public EndpointCall addToHistory(String path, String body) {
        return repo.save(new EndpointCall(path, body));
    }
}
