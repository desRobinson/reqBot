package io.quell.platform.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

public interface JsonString {

    default String toJsonString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(new StringWriter(), this);
        return mapper.writeValueAsString(this);
    }
}
