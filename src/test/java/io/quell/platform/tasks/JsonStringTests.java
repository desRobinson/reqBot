package io.quell.platform.tasks;

import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JsonStringTests {

    @Getter
    @Setter
    class SomeClass implements JsonString {
        String someString;
        Integer someInteger;
    }

    @Test
    public void testToJsonString() throws IOException {
        SomeClass subject = new SomeClass();

        subject.setSomeInteger(123456);
        subject.setSomeString("someTestString");

        assertThat(subject.toJsonString(), is("{\"someString\":\"someTestString\",\"someInteger\":123456}"));
    }
}
