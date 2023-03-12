package com.tempo.challenge.services;

public interface RpmService {
    boolean canProcess();
    void rollback();
}
