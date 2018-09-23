package io.quell.platform.tasks.request;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static io.quell.platform.tasks.TestData.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.POST;

public class StoryWriterTests {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    RestOperations restOperations;

    @Captor
    ArgumentCaptor<HttpEntity> httpEntityCaptor = ArgumentCaptor.forClass(HttpEntity.class);

    StoryWriter subject;

    @Before
    public void setUp() throws URISyntaxException {
        subject = new StoryWriter(restOperations, new URI("stories.uri"));
    }

    @Test
    public void testCreateStoriesFromPlatformMessages() throws IOException {

        PlatformHistory platformHistory = getPlatformRequest(getMockChannelsHistoryResponse("123456789.123456"));

        doReturn(new ResponseEntity<>(HttpStatus.OK))
                .when(restOperations).exchange(any(), eq(POST), httpEntityCaptor.capture(), eq(String.class));

        subject.createStoriesFrom(platformHistory.getMessages());

        HttpEntity<PlatformMessage> storyPost = httpEntityCaptor.getValue();

        assertThat(httpEntityCaptor.getValue().getHeaders().get("Content-Type").get(0), is("application/json"));
        assertThat(storyPost.getBody().getText(), is("<@U9FF4RE8G> The story name"));
    }

    @Test
    public void testCreateStoriesFromPlatformMessagesWhenItDoesntStartWithOurUserHandle() throws IOException {

        PlatformHistory platformHistory = getPlatformRequest(getMockChannelsHistoryResponseWithoutUserHandle("123456789.123456"));

        subject.createStoriesFrom(platformHistory.getMessages());

        verify(restOperations, never()).exchange(any(), any(), any(), eq(String.class));
    }

    @Test
    public void testCreateStoriesFromPlatformMessagesWithMultipleMessages() throws IOException {

        PlatformHistory platformHistory = getPlatformRequest(getMockChannelsHistoryResponseMultipleMessage("123456789.123456", "123456799.123456", "123456999.123456", "123459999.123456"));

        subject.createStoriesFrom(platformHistory.getMessages());

        verify(restOperations, times(2)).exchange(any(), any(), any(), eq(String.class));
    }

    private PlatformHistory getPlatformRequest(String dataToDeserialize) throws IOException {
        return (PlatformHistory) deserialize(dataToDeserialize, PlatformHistory.class);
    }
}