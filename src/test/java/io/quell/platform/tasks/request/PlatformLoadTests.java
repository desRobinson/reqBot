package io.quell.platform.tasks.request;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class PlatformLoadTests {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Captor
    ArgumentCaptor<PlatformMessage> messageArgumentCaptor = ArgumentCaptor.forClass(PlatformMessage.class);

    @Mock
    InfoMessage infoMessage;

    @Mock
    HistoryMessage historyMessage;

    @Mock
    StoryWriter storyWriter;

    @Mock
    MarkMessage markMessage;

    @InjectMocks
    PlatformLoad platformLoad;

    @Test
    public void testProcessRequests() {
        PlatformMessage platformMessage = new PlatformMessage();
        List<PlatformMessage> platformMessages = new ArrayList<>();
        platformMessages.add(platformMessage);

        when(infoMessage.retrieveTimestampOfLastMessageRead()).thenReturn("123499999.123456");
        when(historyMessage.retrieveUnreadMessagesAfter(any(String.class))).thenReturn(platformMessages);
        when(historyMessage.retrieveLastMessage(any())).thenReturn(platformMessage);
        doNothing().when(markMessage).markProcessedMessagesAsRead(messageArgumentCaptor.capture());

        platformLoad.processRequests();

        verify(markMessage, times(1)).markProcessedMessagesAsRead(any());
        assertThat(messageArgumentCaptor.getValue(), is(platformMessage));
    }

    @Test
    public void testProcessRequestsWhenThereAreNoMessages() {

        when(infoMessage.retrieveTimestampOfLastMessageRead()).thenReturn("123499999.123456");
        when(historyMessage.retrieveUnreadMessagesAfter(any(String.class))).thenReturn(new ArrayList<PlatformMessage>());

        platformLoad.processRequests();

        verify(storyWriter, never()).createStoriesFrom(any());
    }
}