package com.tempo.challenge.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EndpointInterceptorExecutor {

    @Async
    public void postHandle(ServerHttpRequest request, ServerHttpResponse response, Object responseBody) {
        log.debug("Request path: " + request.getURI());
        log.debug("Body: " + responseBody);
        log.debug("Call intercptor post ");
    }

}
