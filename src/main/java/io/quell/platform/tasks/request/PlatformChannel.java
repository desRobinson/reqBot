package io.quell.platform.tasks.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quell.platform.tasks.JsonString;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlatformChannel implements JsonString {
    @JsonProperty("last_read")
    String lastRead;
}
