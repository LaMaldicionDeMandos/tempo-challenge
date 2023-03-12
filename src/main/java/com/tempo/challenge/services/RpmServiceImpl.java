package com.tempo.challenge.services;

import org.springframework.stereotype.Service;

@Service
public class RpmServiceImpl implements RpmService {
    @Override
    public boolean canProcess() {
        return false;
    }

    @Override
    public void rollback() {

    }
}
