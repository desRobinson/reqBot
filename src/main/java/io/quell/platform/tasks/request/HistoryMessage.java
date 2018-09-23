package io.quell.platform.tasks.request;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static io.quell.platform.tasks.StringConfig.BEARER;
import static org.springframework.http.HttpMethod.GET;

public class HistoryMessage {

    static final String OLDEST = "oldest";

    RestOperations restOperations;
    URI historyUri;
    String apiToken;

    public HistoryMessage(RestOperations restOperations, URI historyUri, String apiToken) {
        this.restOperations = restOperations;
        this.historyUri = historyUri;
        this.apiToken = apiToken;
    }

    public List<PlatformMessage> retrieveUnreadMessagesAfter(String timestamp) {

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, BEARER + apiToken);

        URI uri = UriComponentsBuilder.fromUri(historyUri).queryParam(OLDEST, timestamp).build().toUri();

        ResponseEntity<PlatformHistory> entity = restOperations.exchange(uri, GET, new HttpEntity<>(httpHeaders), PlatformHistory.class);

        return entity.getBody().getMessages();
    }

    public PlatformMessage retrieveLastMessage(List<PlatformMessage> messages) {
        return messages.get(messages.size() - 1);
    }
}
