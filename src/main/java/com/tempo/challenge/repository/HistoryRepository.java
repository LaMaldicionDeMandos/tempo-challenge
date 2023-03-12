package com.tempo.challenge.repository;

import com.tempo.challenge.model.EndpointCall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<EndpointCall, String> {

    @Query("select e from EndpointCall e")
    Page<EndpointCall> findAll(final Pageable pageable);
}
