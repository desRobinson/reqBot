package io.quell.platform.tasks.request;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class PlatformLoad {

    InfoMessage infoMessage;
    HistoryMessage historyMessage;
    StoryWriter storyWriter;
    MarkMessage markMessage;

    public PlatformLoad(InfoMessage infoMessage, HistoryMessage historyMessage, StoryWriter storyWriter, MarkMessage markMessage) {
        this.infoMessage = infoMessage;
        this.historyMessage = historyMessage;
        this.storyWriter = storyWriter;
        this.markMessage = markMessage;
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 60000)
    public void processRequests() {
        String timestamp = infoMessage.retrieveTimestampOfLastMessageRead();
        List<PlatformMessage> platformMessages = historyMessage.retrieveUnreadMessagesAfter(timestamp);
        if (platformMessages.size() == 0) return;
        Collections.sort(platformMessages);
        storyWriter.createStoriesFrom(platformMessages);
        markMessage.markProcessedMessagesAsRead(historyMessage.retrieveLastMessage(platformMessages));
    }
}
