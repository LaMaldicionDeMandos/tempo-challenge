package com.tempo.challenge.controllers;

import com.tempo.challenge.ChallengeApplication;
import com.tempo.challenge.errors.BusinessException;
import com.tempo.challenge.errors.BusinessModelError;
import com.tempo.challenge.model.EndpointCall;
import com.tempo.challenge.services.HistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(HistoryController.class)
@ContextConfiguration(classes= ChallengeApplication.class)
public class HistoryControllerTest extends AbstractControllerTest<HistoryController>{

    private static final int DEFAULT_OFFSET = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final  String ROOT = "/history";

    @MockBean
    private HistoryService service;

    @MockBean
    private EndpointInterceptor interceptor;

    private Integer offset;
    private Integer pageSize;


    @Override
    protected String getRoot() {
        return ROOT;
    }

    private ResultActions result;

    @Test
    void getHistoryWithDefaultPageValues() throws Exception {
        givenAServiceWithDefaultPage();

        whenTryToGetHistoryWithNoParams();

        thenShouldCallServiceWithDefaultPageData();
        thenShouldNotCallInterceptor();
        thenShouldBeA200WithDefaultPagedData();
    }

    @Test
    void getHistoryWithOffsetAndPageSize() throws Exception {
        givenAOffset();
        givenAPageSize();
        givenAServiceWithOffsetAndPageSize();

        whenTryToGetHistoryWithOffsetAndPageSize();

        thenShouldCallServiceWithOffsetAndPageSizePageData();
        thenShouldNotCallInterceptor();
        thenShouldBeA200WithOffsetAndPagedData();
    }

    @Test
    void getHistoryWithError() throws Exception {
        givenAServiceWithDefaultPageThatHasError();

        whenTryToGetHistoryWithNoParams();

        thenShouldCallServiceWithDefaultPageData();
        thenShouldNotCallInterceptor();
        thenShouldBeA500InternalServerError();
    }

    private void givenAOffset() {
        offset = 5;
    }

    private void givenAPageSize() {
        pageSize = 5;
    }

    private void givenAServiceWithDefaultPage() {
        Page<EndpointCall> page = createPagedData(DEFAULT_OFFSET, DEFAULT_PAGE_SIZE, 5);
        when(service.getHistory(DEFAULT_OFFSET, DEFAULT_PAGE_SIZE)).thenReturn(page);
    }

    private void givenAServiceWithOffsetAndPageSize() {
        Page<EndpointCall> page = createPagedData(offset, pageSize, 10);
        when(service.getHistory(offset, pageSize)).thenReturn(page);
    }

    private void givenAServiceWithDefaultPageThatHasError() {
        when(service.getHistory(DEFAULT_OFFSET, DEFAULT_PAGE_SIZE)).thenThrow(
                new BusinessException(new BusinessModelError("internal_error", "Error with external service."))
        );
    }

    private void whenTryToGetHistoryWithNoParams() throws Exception {
        result = mvc.perform(get(ROOT)
                .header(CONTENT_TYPE, "application/json")).andDo(print());
    }

    private void whenTryToGetHistoryWithOffsetAndPageSize() throws Exception {
        result = mvc.perform(get(ROOT)
                .header(CONTENT_TYPE, "application/json")
                .param("offset", offset.toString())
                .param("size", pageSize.toString())).andDo(print());
    }

    private Page<EndpointCall> createPagedData(final Integer offset, final Integer size, final Integer elements) {
        List<EndpointCall> list = IntStream.range(0, Math.min(elements, size))
                .boxed()
                .map((index) -> new EndpointCall(Integer.valueOf(index).toString(), "PATH", "RESULT"))
                .toList();
        return new PageImpl<>(list, Pageable.ofSize(size).withPage(offset/size), elements);
    }

    private void thenShouldCallServiceWithDefaultPageData() {
        verify(service).getHistory(DEFAULT_OFFSET, DEFAULT_PAGE_SIZE);
    }

    private void thenShouldCallServiceWithOffsetAndPageSizePageData() {
        verify(service).getHistory(offset, pageSize);
    }

    private void thenShouldNotCallInterceptor() {
        verify(interceptor, never()).beforeBodyWrite(any(), any(), any(), any(), any(), any());
        verify(interceptor, never()).supports(any(), any());
    }

    private void thenShouldBeA200WithDefaultPagedData() throws Exception {
        result.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.content", iterableWithSize(5)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(5)))
                .andExpect(jsonPath("$.number", is(0)))
                .andExpect(jsonPath("$.numberOfElements", is(5)))
                .andExpect(jsonPath("$.first", is(true)))
                .andReturn();
    }

    private void thenShouldBeA200WithOffsetAndPagedData() throws Exception {
        result.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.content", iterableWithSize(5)))
                .andExpect(jsonPath("$.totalPages", is(2)))
                .andExpect(jsonPath("$.totalElements", is(10)))
                .andExpect(jsonPath("$.number", is(1)))
                .andExpect(jsonPath("$.numberOfElements", is(5)))
                .andExpect(jsonPath("$.first", is(false)))
                .andReturn();
    }

    private void thenShouldBeA500InternalServerError() throws Exception {
        result.andExpect(status().isInternalServerError())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.error_code", is("internal_error")))
                .andExpect(jsonPath("$.error_message", is("Error with external service.")))
                .andReturn();
    }
}
