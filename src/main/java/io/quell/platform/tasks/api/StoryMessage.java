package io.quell.platform.tasks.api;

import io.quell.platform.tasks.JsonString;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StoryMessage implements JsonString {
    String name;
    String description;

    public boolean empty() {
        return name == null && description == null;
    }
}
