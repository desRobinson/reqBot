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
import static org.mockito.Mockito.*;

public class StoryServiceTests {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Captor
    ArgumentCaptor<StoryMessage> storyMessageArgumentCaptor = ArgumentCaptor.forClass(StoryMessage.class);

    @Mock
    StoryPublisher storyPublisher;

    @InjectMocks
    StoryService subject;

    ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testCreate() throws IOException {

        doNothing().when(storyPublisher).send(storyMessageArgumentCaptor.capture());
        StoryRequest storyRequest = generateStoryRequestObject("<@U9FF4RE8G> name: The story name description: The story description");

        subject.create(storyRequest);

        verify(storyPublisher, times(1)).send(any(StoryMessage.class));
        assertThat(storyMessageArgumentCaptor.getValue().getName(), is("The story name"));
        assertThat(storyMessageArgumentCaptor.getValue().getDescription(), is("The story description"));
    }

    @Test
    public void testCreateWithEmptyParseMessage() throws IOException {
        StoryRequest storyRequest = generateStoryRequestObject("<@U9FF4RE8G>");

        subject.create(storyRequest);

        verify(storyPublisher, times(0)).send(any(StoryMessage.class));
    }

    private StoryRequest generateStoryRequestObject(String text) throws IOException {

        String mockMessage = "{\n" +
                "    \"type\": \"app_mention\",\n" +
                "    \"user\": \"user-making-tasks\",\n" +
                "    \"text\": \"" + text + "\",\n" +
                "    \"ts\": \"timestamp\",\n" +
                "    \"channel\": \"channel-name\",\n" +
                "    \"event_ts\": \"event-timestamp\"\n" +
                "}";

        return mapper.readValue(mockMessage, StoryRequest.class);
    }
}