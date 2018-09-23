package io.quell.platform.tasks.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;

public class StoryControllerTests {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    StoryService mockStoryService;

    @InjectMocks
    StoryController subject;

    @Captor
    ArgumentCaptor<StoryRequest> storyRequestArgumentCaptor = ArgumentCaptor.forClass(StoryRequest.class);

    ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testCreate() throws IOException {
        String mockMessage = "{\n" +
                "    \"type\": \"app_mention\",\n" +
                "    \"user\": \"user-making-tasks\",\n" +
                "    \"text\": \"<@service-receiving-tasks> The story name\",\n" +
                "    \"ts\": \"timestamp\",\n" +
                "    \"channel\": \"channel-name\",\n" +
                "    \"event_ts\": \"event-timestamp\"\n" +
                "}";

        StoryRequest storyRequest = mapper.readValue(mockMessage, StoryRequest.class);

        doNothing().when(mockStoryService).create(storyRequestArgumentCaptor.capture());

        subject.create(storyRequest);

        assertThat(storyRequestArgumentCaptor.getValue().getText(), is("<@service-receiving-tasks> The story name"));
    }
}