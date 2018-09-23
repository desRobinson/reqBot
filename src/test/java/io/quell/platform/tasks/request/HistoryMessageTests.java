package io.quell.platform.tasks.request;

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
import java.util.List;

import static io.quell.platform.tasks.TestData.deserialize;
import static io.quell.platform.tasks.TestData.getMockChannelsHistoryResponse;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpMethod.GET;

public class HistoryMessageTests {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    RestOperations restOperations;

    @Captor
    ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);

    @Captor
    ArgumentCaptor<HttpEntity> httpEntityCaptor = ArgumentCaptor.forClass(HttpEntity.class);

    HistoryMessage subject;

    @Test
    public void testRetrieveUnreadMessagesAfterTimestamp() throws IOException, URISyntaxException {

        PlatformHistory platformHistory = getPlatformRequest(getMockChannelsHistoryResponse("999999999.123456"));

        doReturn(new ResponseEntity<>(platformHistory, HttpStatus.OK))
                .when(restOperations).exchange(uriCaptor.capture(), eq(GET), httpEntityCaptor.capture(), eq(PlatformHistory.class));

        subject = new HistoryMessage(restOperations, new URI("history.uri"), "somePlatformToken");
        List<PlatformMessage> platformMessages = subject.retrieveUnreadMessagesAfter("123499999.123456");

        // check that our URL is passing the right timestamp through
        // check that our header
        // check that we get a list of messages
        assertThat(uriCaptor.getValue().toString(), is("history.uri?oldest=123499999.123456"));
        assertThat(httpEntityCaptor.getValue().getHeaders().get("Content-Type").get(0), is("application/json"));
        assertThat(httpEntityCaptor.getValue().getHeaders().get("Authorization").get(0), is("Bearer somePlatformToken"));
        assertThat(platformMessages.get(0).getText(), is("<@U9FF4RE8G> The story name"));
    }

    @Test
    public void testRetrieveLastMessage() throws URISyntaxException, IOException {

        PlatformHistory platformHistory = getPlatformRequest(getMockChannelsHistoryResponse("999999999.123456"));

        subject = new HistoryMessage(restOperations, new URI("history.uri"), "somePlatformToken");
        PlatformMessage platformMessage = subject.retrieveLastMessage(platformHistory.getMessages());

        assertThat(platformMessage, is(platformHistory.getMessages().get(0)));
    }

    private PlatformHistory getPlatformRequest(String dataToDeserialize) throws IOException {
        return (PlatformHistory) deserialize(dataToDeserialize, PlatformHistory.class);
    }
}