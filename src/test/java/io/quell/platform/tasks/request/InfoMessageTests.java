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
import static io.quell.platform.tasks.TestData.getMockChannelsInfoResponse;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpMethod.GET;

public class InfoMessageTests {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    RestOperations restOperations;

    @Captor
    ArgumentCaptor<HttpEntity> httpEntityCaptor = ArgumentCaptor.forClass(HttpEntity.class);

    InfoMessage subject;

    @Test
    public void testRetrieveTimestampOfLastMessageRead() throws IOException, URISyntaxException {

        PlatformInfo platformInfo = getPlatformInfo(getMockChannelsInfoResponse("123499999.123456"));
        doReturn(new ResponseEntity<>(platformInfo, HttpStatus.OK))
                 .when(restOperations).exchange(any(),
                                                eq(GET),
                                                httpEntityCaptor.capture(),
                                                eq(PlatformInfo.class));

        URI uri = new URI("info");
        subject = new InfoMessage(restOperations, uri, "somePlatformToken");

        String timestamp = subject.retrieveTimestampOfLastMessageRead();

        assertThat(timestamp, is("123499999.123456"));
        assertThat(httpEntityCaptor.getValue().getHeaders().get("Content-Type").get(0), is("application/json"));
        assertThat(httpEntityCaptor.getValue().getHeaders().get("Authorization").get(0), is("Bearer somePlatformToken"));
    }

    private PlatformInfo getPlatformInfo(String dataToDeserialize) throws IOException {
        return (PlatformInfo) deserialize(dataToDeserialize, PlatformInfo.class);
    }
}