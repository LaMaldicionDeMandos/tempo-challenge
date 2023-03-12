package com.tempo.challenge.repository;

import com.tempo.challenge.model.EndpointCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<EndpointCall, String> {
}
