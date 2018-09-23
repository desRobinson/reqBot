package io.quell.platform.tasks.api;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class StoryPublisherTests {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    RestTemplate restTemplate;

    @Mock
    StoryMessage mockStoryMessage;

    @Captor
    ArgumentCaptor<String> uriCaptor = ArgumentCaptor.forClass(String.class);

    @Captor
    ArgumentCaptor<HttpEntity> httpEntityCaptor = ArgumentCaptor.forClass(HttpEntity.class);

    @Test
    public void testSend() throws IOException {

        when(restTemplate.postForEntity(uriCaptor.capture(), httpEntityCaptor.capture(), any())).thenReturn(null);

        StoryPublisher subject = new StoryPublisher(restTemplate, "service/123456/call", "abcdefghijklm");

        subject.send(mockStoryMessage);

        assertThat(uriCaptor.getValue(), is("service/123456/call"));
        assertThat(httpEntityCaptor.getValue().getHeaders().get("X-TrackerToken").get(0), is("abcdefghijklm"));
    }
}