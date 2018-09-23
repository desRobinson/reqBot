package io.quell.platform.tasks.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class StoryService {

    StoryPublisher storyPublisher;

    @Autowired
    public StoryService(StoryPublisher storyPublisher) {
        this.storyPublisher = storyPublisher;
    }

    public void create(StoryRequest storyRequest) throws IOException {
        StoryMessage storyMessage = StoryParser.parse(storyRequest.getText());

        if (!storyMessage.empty()) {
            storyPublisher.send(storyMessage);
        }
    }
}
