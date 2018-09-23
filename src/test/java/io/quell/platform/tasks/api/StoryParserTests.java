package io.quell.platform.tasks.api;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.assertThat;

public class StoryParserTests {

    @Test
    public void testParse() {
        String textToParse = "<@U9FF4RE8G> name: The name description: The description";

        StoryMessage message = StoryParser.parse(textToParse);

        assertThat(message.getName(), is("The name"));
        assertThat(message.getDescription(), is("The description"));
    }

    @Test
    public void testParseWithMixedCaseNameAndDescriptionTags() {
        String textToParse = "<@U9FF4RE8G> NaMe: The name DescrIption: The description";

        StoryMessage message = StoryParser.parse(textToParse);

        assertThat(message.getName(), is("The name"));
        assertThat(message.getDescription(), is("The description"));
    }

    @Test
    public void testParseNoSpaces() {
        String textToParse = "<@U9FF4RE8G>name: The name description: The description";

        StoryMessage message = StoryParser.parse(textToParse);

        assertThat(message.getName(), is("The name"));
        assertThat(message.getDescription(), is("The description"));
    }

    @Test
    public void testParseWithOnlyAName() {
        String textToParse = "<@U9FF4RE8G> name: The name";

        StoryMessage message = StoryParser.parse(textToParse);

        assertThat(message.getName(), is("The name"));
    }

    @Test
    public void testParseWithOnlyADescription() {
        String textToParse = "<@U9FF4RE8G> description: The description";

        StoryMessage message = StoryParser.parse(textToParse);

        assertThat(message.getDescription(), is("The description"));
    }

    @Test
    public void testParseWithNeitherANameNorDescription() {
        String textToParse = "<@U9FF4RE8G> Rambling text without a name or description";

        StoryMessage message = StoryParser.parse(textToParse);

        assertThat(message.getName(), is("Rambling text without a name or description"));
        assertThat(message.getDescription(), is("Rambling text without a name or description"));
    }

    @Test
    public void testParseWithUsernameOnly() {
        String textToParse = "<@U9FF4RE8G>";

        StoryMessage message = StoryParser.parse(textToParse);

        assertThat(message.getName(), isEmptyOrNullString());
        assertThat(message.getDescription(), isEmptyOrNullString());
    }

    @Test
    public void testParseWithUsernameAndSingleWord() {
        String textToParse = "<@U9FF4RE8G> word";

        StoryMessage message = StoryParser.parse(textToParse);

        assertThat(message.getName(), is("word"));
        assertThat(message.getDescription(), is("word"));
    }

    @Test
    public void testParseNoNameButFreeFormTextAndNormalDescription() {
        String textToParse = "<@U9FF4RE8G> userid: `sometag` description: Please create this user if not exists and add to `someOtherTag in AD, thank you!";

        StoryMessage message = StoryParser.parse(textToParse);

        assertThat(message.getName(), is("userid: `sometag`"));
        assertThat(message.getDescription(), is("Please create this user if not exists and add to `someOtherTag in AD, thank you!"));
    }
}
