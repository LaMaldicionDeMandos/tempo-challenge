package com.tempo.challenge.services;

import com.tempo.challenge.model.EndpointCall;
import com.tempo.challenge.repository.HistoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.IntStream;

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

    private int offset;
    private int pageSize;

    private EndpointCall result;
    private Page<EndpointCall> expected;
    private Page<EndpointCall> page;

    @Test
    void newEndpointCall() {
        givenAPath();
        givenABody();

        whenAddToHistory();

        thenShouldSaveEndpointCall();
    }

    @Test
    void getHistory() {
        givenAOffset();
        givenAPageSize();
        givenARepoThatReturnData();

        whenTryToGetHistory();

        thenShouldCallHistoryRepo();
        thenShouldReturnPagedData();
    }

    private void givenAPath() {
        path = "/calc/plus?number1=5&number2=5";
    }

    private void givenABody() {
        body = "{\"result\":11}";
    }

    private void givenAOffset() {
        offset = 5;
    }

    private void givenAPageSize() {
        pageSize = 5;
    }

    private void givenARepoThatReturnData() {
        expected = createPagedData(offset, pageSize, 10);
        Pageable pageable = Pageable.ofSize(pageSize).withPage(1);
        when(repo.findAll(pageable)).thenReturn(expected);
    }

    private void whenAddToHistory() {
        when(repo.save(any(EndpointCall.class))).thenAnswer((answer) -> answer.getArgument(0));
        result = historyService.addToHistory(path, body);
    }

    private void whenTryToGetHistory() {
        page = historyService.getHistory(offset, pageSize);
    }

    private void thenShouldSaveEndpointCall() {
        EndpointCall expected = new EndpointCall(path, body);
        verify(repo).save(any(EndpointCall.class));
        assertEquals(expected.getPath(), result.getPath());
        assertEquals(expected.getResult(), result.getResult());
    }

    private void thenShouldCallHistoryRepo() {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(1);
        verify(repo).findAll(pageable);
    }

    private void thenShouldReturnPagedData() {assertEquals(expected, page);
    }

    private Page<EndpointCall> createPagedData(final Integer offset, final Integer size, final Integer elements) {
        List<EndpointCall> list = IntStream.range(0, Math.min(elements, size))
                .boxed()
                .map((index) -> new EndpointCall(Integer.valueOf(index).toString(), "PATH", "RESULT"))
                .toList();
        return new PageImpl<>(list, Pageable.ofSize(size).withPage(offset/size), elements);
    }
}
