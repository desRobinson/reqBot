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

import static io.quell.platform.tasks.TestData.deserialize;
import static io.quell.platform.tasks.TestData.getMockChannelsHistoryResponse;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpMethod.POST;


public class MarkMessageTests {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    RestOperations restOperations;

    @Captor
    ArgumentCaptor<HttpEntity> httpEntityCaptor = ArgumentCaptor.forClass(HttpEntity.class);

    MarkMessage subject;

    @Test
    public void testMarkProcessedMessagesAsRead() throws URISyntaxException, IOException {

        PlatformHistory platformHistory = getPlatformRequest(getMockChannelsHistoryResponse("999999999.123456"));

        doReturn(new ResponseEntity<>(HttpStatus.OK))
                .when(restOperations).exchange(any(), eq(POST), httpEntityCaptor.capture(), eq(String.class));

        subject = new MarkMessage(restOperations, new URI("mark.uri"), "somePlatformToken", "testchannel");
        subject.markProcessedMessagesAsRead(platformHistory.getMessages().get(0));

        HttpEntity<PlatformMark> markPost = httpEntityCaptor.getValue();

        assertThat(httpEntityCaptor.getValue().getHeaders().get("Content-Type").get(0), is("application/json"));
        assertThat(httpEntityCaptor.getValue().getHeaders().get("Authorization").get(0), is("Bearer somePlatformToken"));
        assertThat(markPost.getBody().getChannel(), is("testchannel"));
        assertThat(markPost.getBody().getTimestamp(), is("999999999.123456"));
    }

    private PlatformHistory getPlatformRequest(String dataToDeserialize) throws IOException {
        return (PlatformHistory) deserialize(dataToDeserialize, PlatformHistory.class);
    }

}