package com.tempo.challenge.services;

import com.tempo.challenge.model.EndpointCall;
import com.tempo.challenge.repository.HistoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class HistoryServiceTest {

    @Autowired
    private HistoryService historyService;

    @MockBean
    HistoryRepository repo;

    private String path;
    private String body;

    private EndpointCall result;

    @Test
    void newEndpointCall() {
        givenAPath();
        givenABody();

        whenAddToHistory();

        thenShouldSaveEndpointCall();
    }

    private void givenAPath() {
        path = "/calc/plus?number1=5&number2=5";
    }

    private void givenABody() {
        body = "{\"result\":11}";
    }

    private void whenAddToHistory() {
        when(repo.save(any(EndpointCall.class))).thenAnswer((answer) -> answer.getArgument(0));
        result = historyService.addToHistory(path, body);
    }

    private void thenShouldSaveEndpointCall() {
        EndpointCall expected = new EndpointCall(path, body);
        verify(repo).save(any(EndpointCall.class));
        assertEquals(expected.getPath(), result.getPath());
        assertEquals(expected.getResult(), result.getResult());
    }
}
