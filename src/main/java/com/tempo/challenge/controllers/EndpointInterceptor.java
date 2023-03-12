package com.tempo.challenge.controllers;


import com.tempo.challenge.dto.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Component
@Slf4j
@ControllerAdvice(assignableTypes = CalcController.class)
public class EndpointInterceptor implements ResponseBodyAdvice<Object> {
    private EndpointInterceptorExecutor executor;

    @Autowired
    public EndpointInterceptor(final EndpointInterceptorExecutor executor) {
        this.executor = executor;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        executor.postHandle(request, body);
        return body;
    }
}
