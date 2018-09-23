package io.quell.platform.tasks.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestOperations;

import java.io.IOException;

public class StoryPublisher {

    final static String X_TRACKER_TOKEN = "X-TrackerToken";
    final static String CONTENT_TYPE = "Content-Type";

    RestOperations restOperations;
    String url;
    String apiToken;

    @Autowired
    public StoryPublisher(RestOperations restOperations, String url, String apiToken) {
        this.restOperations = restOperations;
        this.url = url;
        this.apiToken = apiToken;
    }

    public void send(StoryMessage storyMessage) throws IOException {
        restOperations.postForEntity(url, buildHttpEntity(storyMessage.toJsonString()), String.class);
    }

    private HttpEntity<String> buildHttpEntity(String body) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add(X_TRACKER_TOKEN, this.apiToken);
        httpHeaders.add(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return new HttpEntity<>(body, httpHeaders);
    }
}
