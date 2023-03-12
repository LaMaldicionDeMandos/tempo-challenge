package com.tempo.challenge.repository;

import com.tempo.challenge.model.EndpointCall;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class HistoryRepositoryTest {

    @Autowired
    private HistoryRepository repo;

    private EndpointCall endpointCall;

    private EndpointCall result;

    @Test
    void insertEndpointCall() {
        givenAnEndpointCall();

        whenTryToSaveAnEndpointCall();

        thenResultShouldBeHasAnId();
        thenResultShouldBeHasAnValidValues();
    }

    private void givenAnEndpointCall() {
        endpointCall = new EndpointCall("saraza", "fruta");
    }

    private void whenTryToSaveAnEndpointCall() {
        result = repo.save(endpointCall);
    }

    private void thenResultShouldBeHasAnId() {
        assertNotNull(result.getId());
    }

    private void thenResultShouldBeHasAnValidValues() {
        assertEquals(endpointCall.getPath(), result.getPath());
        assertEquals(endpointCall.getResult(), result.getResult());
    }
}
