package io.quell.platform.tasks.request;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;

import java.net.URI;

import static io.quell.platform.tasks.StringConfig.BEARER;
import static org.springframework.http.HttpMethod.GET;

public class InfoMessage {

    RestOperations restOperations;
    URI uri;
    String apiToken;

    public InfoMessage(RestOperations restOperations, URI uri, String apiToken) {
        this.restOperations = restOperations;
        this.uri = uri;
        this.apiToken = apiToken;
    }

    public String retrieveTimestampOfLastMessageRead() {

        PlatformInfo info = makeHttpCallToRetrievePlatformInfo().getBody();
        PlatformChannel channel =  info.getChannel();

        return channel.getLastRead();
    }

    private ResponseEntity<PlatformInfo> makeHttpCallToRetrievePlatformInfo() {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, BEARER + apiToken);

        ResponseEntity<PlatformInfo> entity = restOperations.exchange(uri, GET, new HttpEntity<>(httpHeaders), PlatformInfo.class);

        return entity;
    }
}
