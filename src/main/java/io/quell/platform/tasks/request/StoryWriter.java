package io.quell.platform.tasks.request;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpMethod.POST;

public class StoryWriter {

    static final String platformUsername = "<@U9FF4RE8G>";

    RestOperations restOperations;
    URI uri;

    public StoryWriter(RestOperations restOperations, URI uri) {
        this.restOperations = restOperations;
        this.uri = uri;
    }

    public void createStoriesFrom(List<PlatformMessage> messages) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        for (PlatformMessage message: messages) {
            if(message.getText().startsWith(platformUsername)) {
                restOperations.exchange(uri, POST, new HttpEntity<>(message, httpHeaders), String.class);
            }
        }
    }
}
