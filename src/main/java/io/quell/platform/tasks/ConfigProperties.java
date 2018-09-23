package io.quell.platform.tasks;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigProperties {
    String projectId;
    String platformRequestToken;
    String storyToken;
    String storyUri;
    String infoUri;
    String historyUri;
    String markUri;
    String channel;
    String internal;
}
