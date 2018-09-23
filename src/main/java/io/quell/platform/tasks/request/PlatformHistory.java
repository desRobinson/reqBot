package io.quell.platform.tasks.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.quell.platform.tasks.JsonString;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlatformHistory implements JsonString {
    List<PlatformMessage> messages;
}
