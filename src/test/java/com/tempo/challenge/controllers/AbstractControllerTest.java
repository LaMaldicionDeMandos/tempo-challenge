package com.tempo.challenge.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;


public abstract class AbstractControllerTest<T> {
    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected T controller;

    protected abstract String getRoot();

    public static String getJson(Object object) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }
}

