package io.quell.platform.tasks.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quell.platform.tasks.JsonString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlatformMark implements JsonString {
    String channel;
    @JsonProperty("ts")
    String timestamp;
}
