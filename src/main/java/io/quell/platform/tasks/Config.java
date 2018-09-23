package io.quell.platform.tasks;

import io.quell.platform.tasks.api.StoryPublisher;
import io.quell.platform.tasks.request.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class Config {

    private static final String CHANNEL = "channel";

    @Value("${downstream.server.url}")
    String storyUri;

    @Value("${downstream.server.apitoken}")
    String storyToken;

    @Value("${platform.request.apitoken}")
    String platformRequestToken;

    @Value("${downstream.server.projectid}")
    String projectId;

    @Value("${platform.request.channel}")
    String channel;

    @Value("${platform.request.path.info}")
    String info;

    @Value("${platform.request.path.history}")
    String history;

    @Value("${platform.request.path.mark}")
    String mark;

    @Value("${platform.request.path.internal}")
    String internal;

    @Bean
    public ConfigProperties configProperties() {

        ConfigProperties configProperties = new ConfigProperties();

        configProperties.setProjectId(projectId);
        configProperties.setStoryToken(storyToken);
        configProperties.setPlatformRequestToken(platformRequestToken);
        configProperties.setStoryUri(storyUri);
        configProperties.setInfoUri(buildUriWithChannel(info));
        configProperties.setHistoryUri(buildUriWithChannel(history));
        configProperties.setMarkUri(mark);
        configProperties.setChannel(channel);
        configProperties.setInternal(internal);

        return configProperties;
    }

    private String buildUriWithChannel(String uri) {
        MultiValueMap<String, String> queryParts = new LinkedMultiValueMap<>();
        queryParts.add(CHANNEL, channel);

        return UriComponentsBuilder.fromUriString(uri).queryParams(queryParts).build().toUriString();
    }

    @Bean
    public RestOperations restOperations() {
        return new RestTemplate();
    }

    @Bean
    public StoryPublisher storyPublisher(RestOperations restOperations, ConfigProperties configProperties) {
        String storyUri = String.format(configProperties.getStoryUri(), configProperties.getProjectId());
        return new StoryPublisher(restOperations, storyUri, configProperties.getStoryToken());
    }

    @Bean
    public InfoMessage infoMessage(RestOperations restOperations, ConfigProperties configProperties) throws URISyntaxException {
        return new InfoMessage(restOperations, new URI(configProperties.getInfoUri()), configProperties.getPlatformRequestToken());
    }

    @Bean
    public HistoryMessage historyMessage(RestOperations restOperations, ConfigProperties configProperties) throws URISyntaxException {
        return new HistoryMessage(restOperations, new URI(configProperties.getHistoryUri()), configProperties.getPlatformRequestToken());
    }

    @Bean
    public StoryWriter storyWriter(RestOperations restOperations, ConfigProperties configProperties) throws URISyntaxException {
        return new StoryWriter(restOperations, new URI(String.format(configProperties.getInternal(), configProperties.getProjectId())));
    }

    @Bean
    public MarkMessage markMessage(RestOperations restOperations, ConfigProperties configProperties) throws URISyntaxException {
        return new MarkMessage(restOperations, new URI(configProperties.getMarkUri()), configProperties.getPlatformRequestToken(), configProperties.getChannel());
    }

    @Bean
    public PlatformLoad platformLoad(InfoMessage infoMessage, HistoryMessage historyMessage, MarkMessage markMessage, StoryWriter storyWriter) {
        return new PlatformLoad(infoMessage, historyMessage, storyWriter, markMessage);
    }
}
