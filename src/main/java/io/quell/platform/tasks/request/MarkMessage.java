package io.quell.platform.tasks.request;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestOperations;

import java.net.URI;

import static io.quell.platform.tasks.StringConfig.BEARER;
import static org.springframework.http.HttpMethod.POST;

public class MarkMessage {

    RestOperations restOperations;
    URI uri;
    String apiToken;
    String channel;

    public MarkMessage(RestOperations restOperations, URI uri, String apiToken, String channel) {
        this.restOperations = restOperations;
        this.uri = uri;
        this.apiToken = apiToken;
        this.channel = channel;
    }

    public void markProcessedMessagesAsRead(PlatformMessage message) {

        PlatformMark platformMark = new PlatformMark();
        platformMark.setChannel(channel);
        platformMark.setTimestamp(message.getTimestamp());

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, BEARER + apiToken);

        HttpEntity<PlatformMark> httpEntity = new HttpEntity<>(platformMark, httpHeaders);

        restOperations.exchange(uri, POST, httpEntity, String.class);
    }
}
