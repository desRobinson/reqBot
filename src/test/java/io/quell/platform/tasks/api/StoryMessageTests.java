package io.quell.platform.tasks.api;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class StoryMessageTests {

    @Test
    public void testEmpty() {
        StoryMessage subject = StoryMessage.builder().build();

        boolean empty = subject.empty();

        assertTrue(empty);
    }
}