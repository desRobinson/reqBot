package io.quell.platform.tasks.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quell.platform.tasks.JsonString;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlatformMessage implements JsonString, Comparable {
    String type;
    String user;
    String text;
    @JsonProperty("ts")
    String timestamp;

    @Override
    public int compareTo(Object o) {
        PlatformMessage message = (PlatformMessage) o;
        if(this.retrieveTimestampAsLong() == message.retrieveTimestampAsLong()) {
            return 0;
        }else{
            return (this.retrieveTimestampAsLong() > message.retrieveTimestampAsLong() ? 1 : -1);
        }
    }

    public long retrieveTimestampAsLong(){
        return Long.parseLong(this.getTimestamp().replace(".", ""));
    }
}
