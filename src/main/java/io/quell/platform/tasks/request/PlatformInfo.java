package io.quell.platform.tasks.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.quell.platform.tasks.JsonString;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlatformInfo  implements JsonString {
    PlatformChannel channel;
}
