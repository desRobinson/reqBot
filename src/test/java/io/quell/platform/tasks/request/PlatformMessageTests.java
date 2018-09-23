package io.quell.platform.tasks.request;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PlatformMessageTests {

    @Test
    public void testCompare() {
        String unixDateEarly = "1523630126.000122";
        String unixDateMiddle = "1523630126.000123";
        String unixDateLate = "1523630126.000124";

        PlatformMessage platformMessageEarly = new PlatformMessage();
        PlatformMessage platformMessageMiddle = new PlatformMessage();
        PlatformMessage platformMessageLate = new PlatformMessage();

        ReflectionTestUtils.setField(platformMessageEarly, "timestamp", unixDateEarly);
        ReflectionTestUtils.setField(platformMessageMiddle, "timestamp", unixDateMiddle);
        ReflectionTestUtils.setField(platformMessageLate, "timestamp", unixDateLate);

        List<PlatformMessage> platformMessages = new ArrayList<>();
        platformMessages.add(platformMessageMiddle);
        platformMessages.add(platformMessageLate);
        platformMessages.add(platformMessageEarly);

        assertThat(platformMessages.get(0), is(platformMessageMiddle));
        assertThat(platformMessages.get(1), is(platformMessageLate));
        assertThat(platformMessages.get(2), is(platformMessageEarly));

        Collections.sort(platformMessages);

        assertThat(platformMessages.get(0), is(platformMessageEarly));
        assertThat(platformMessages.get(1), is(platformMessageMiddle));
        assertThat(platformMessages.get(2), is(platformMessageLate));
    }

    @Test
    public void testRetrieveTimestampAsLong() {

        PlatformMessage subject = new PlatformMessage();
        ReflectionTestUtils.setField(subject, "timestamp", "1523630126.000122");

        assertThat(subject.retrieveTimestampAsLong(), is(1523630126000122L));
    }
}