package com.tempo.challenge.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tempo.challenge.services.HistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
@Slf4j
public class EndpointInterceptorExecutor {

    private final HistoryService historyService;

    @Autowired
    public EndpointInterceptorExecutor(final HistoryService historyService) {
        this.historyService = historyService;
    }

    @Async
    public void postHandle(ServerHttpRequest request, Object responseBody) {
        log.debug("Request path: " + request.getURI());
        log.debug("Body: " + responseBody);
        String path = request.getURI().getPath();
        String params = request.getURI().getQuery();
        String bodyJson = serializeJson(responseBody);

        String p = assemblePath(path, params);
        historyService.addToHistory(p, bodyJson);
    }

    private String assemblePath(final String path, final String params) {
        return isNull(params) || params.isBlank() ? path : path + "?" + params;
    }

    public String serializeJson(Object o) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String temp = "";

        try {
            temp = objectMapper.writeValueAsString(o);
        }
        catch(JsonProcessingException e){
            return null;
        }
        return temp;
    }

}
