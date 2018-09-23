package io.quell.platform.tasks.api;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class StoryParser {

    static final String CASELESS = "(?i)";
    static final String LOOK_AHEAD = "(?=%1$s)";
    static final String LOOK_BEHIND = "(?<=%1$s)";
    static final String OR = "|";
    static final String LOOK_AHEAD_OR_BEHIND = "(" + LOOK_AHEAD + OR + LOOK_BEHIND + ")";

    static final String USER_ID = "^<@\\w+>";
    static final String USER_ID_ONLY = USER_ID + "$";
    static final String NAME = "name:";
    static final String DESCRIPTION = "description:";

    static final String SPACE = " ";
    static final String EMPTY_STRING = "";

    static final int ZERO = 0;
    static final int ONE = 1;
    static final int TWO = 2;

    static final String[] regex = {
            String.format(LOOK_BEHIND, ">") + String.format(LOOK_AHEAD, ".") + OR + String.format(LOOK_BEHIND, "\\s") + String.format(LOOK_AHEAD, "<"),
            String.format(LOOK_AHEAD_OR_BEHIND, CASELESS + NAME),
            String.format(LOOK_AHEAD_OR_BEHIND, CASELESS + DESCRIPTION)
    };

    public static StoryMessage parse(String text) {

        Map<String, String> nameAndDescription = (textOnlyHasTheUserName(SPACE + text)) ? new HashMap<>() : parseMessage(text);

        return StoryMessage.builder()
                .name(nameAndDescription.get(NAME))
                .description(nameAndDescription.get(DESCRIPTION))
                .build();
    }

    private static Map<String, String> parseMessage(String text) {

        if (elementPairFound(text)) {
            return populateNameAndDescription(text);
        } else {
            return populateNameAndDescriptionWithDefaults(text);
        }
    }

    private static Map<String, String> populateNameAndDescription(String text) {
        Map<String, String> nameAndDescription = new HashMap<>();

        for (int i = regex.length - ONE; i >= ZERO; i--) {
            String[] elements = text.split(regex[i]);

            if (elementPairFound(elements)) {
                nameAndDescription.put(elements[ONE].toLowerCase(), defaultOrUse(elements[TWO]));
                text = elements[ZERO];
            } else if (elementSingletonFound(elements)) {
                nameAndDescription = populateNameAndDescriptionWithDefaults(text, nameAndDescription);
            }
        }

        return nameAndDescription;
    }

    private static Map<String, String> populateNameAndDescriptionWithDefaults(String text) {
        Map<String, String> nameAndDescription = new HashMap<>();
        return populateNameAndDescriptionWithDefaults(text, nameAndDescription);
    }

    private static Map<String, String> populateNameAndDescriptionWithDefaults(String text, Map<String, String> nameAndDescription) {
        String[] splitOfUserIdAndText = text.trim().split(regex[ZERO]);

        nameAndDescription.putIfAbsent(NAME, splitOfUserIdAndText[ONE].trim());
        nameAndDescription.putIfAbsent(DESCRIPTION, splitOfUserIdAndText[ONE].trim());

        return nameAndDescription;
    }

    private static String defaultOrUse(String text) {
        return EMPTY_STRING.equals(text) ? EMPTY_STRING : text.trim();
    }

    private static boolean elementSingletonFound(String[] split) {
        return split.length > ONE && !StringUtils.isEmpty(split[ONE].trim());
    }

    private static boolean elementPairFound(String[] split) {
        return split.length > TWO;
    }

    private static boolean elementPairFound(String text) {
        return (text.toLowerCase().contains(NAME)) || (text.toLowerCase().contains(DESCRIPTION));
    }

    private static boolean textOnlyHasTheUserName(String text) {
        return text.trim().matches(USER_ID_ONLY);
    }
}
