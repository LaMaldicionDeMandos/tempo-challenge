package com.tempo.challenge.repository;

import com.tempo.challenge.model.EndpointCall;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class HistoryRepositoryTest {

    @Autowired
    private HistoryRepository repo;

    private Pageable pageable;

    private EndpointCall endpointCall;

    private EndpointCall result;
    private Page<EndpointCall> page;

    @Test
    void insertEndpointCall() {
        givenAnEndpointCall();

        whenTryToSaveAnEndpointCall();

        thenResultShouldBeHasAnId();
        thenResultShouldBeHasAnValidValues();
    }

    @Test
    void findAllWithPagination() {
        givenAPageable();

        whenTryToFindAllWithPageable();

        thenShouldReturnCorrectInfo();
    }


    private void givenAnEndpointCall() {
        endpointCall = new EndpointCall("saraza", "fruta");
    }

    private void givenAPageable() {
        pageable = PageRequest.of(1, 2);
    }

    private void whenTryToSaveAnEndpointCall() {
        result = repo.save(endpointCall);
    }

    private void whenTryToFindAllWithPageable() {
        page = repo.findAll(pageable);
    }

    private void thenResultShouldBeHasAnId() {
        assertNotNull(result.getId());
    }

    private void thenResultShouldBeHasAnValidValues() {
        assertEquals(endpointCall.getPath(), result.getPath());
        assertEquals(endpointCall.getResult(), result.getResult());
    }

    private void thenShouldReturnCorrectInfo() {
        List<EndpointCall> calls = page.toList();
        assertEquals(5l, page.getTotalElements());
        assertEquals(2, page.getNumberOfElements());
        assertEquals(1, page.getNumber());
        assertEquals(3, page.getTotalPages());
        assertEquals(2, calls.size());
    }
}
