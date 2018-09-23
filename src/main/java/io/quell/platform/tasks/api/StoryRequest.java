package io.quell.platform.tasks.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quell.platform.tasks.JsonString;
import lombok.Getter;

@Getter
public class StoryRequest implements JsonString {
    String type;
    String user;
    String text;
    @JsonProperty("ts")
    String timestamp;
    String channel;
    @JsonProperty("event_ts")
    String eventTimestamp;
}

