package io.quell.platform.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;

public class TestData {

    public static String getMockChannelsMarkResponse() {
        return "{\"ok\": true}";
    }

    public static ConfigProperties getConfigProperties() {
        ConfigProperties configProperties = new ConfigProperties();
        configProperties.setChannel("testchannel");
        configProperties.setInfoUri("info.uri");
        configProperties.setHistoryUri("history.uri");
        configProperties.setMarkUri("mark.uri");
        configProperties.setInternal("story.uri");
        configProperties.setPlatformRequestToken("somePlatformToken");
        configProperties.setStoryToken("someStoryToken");
        configProperties.setProjectId("20202020");

        return configProperties;
    }

    public static String getMockChannelsHistoryResponse(String currentTimestamp) {
        return "{\n" +
                "  \"ok\": true,\n" +
                "  \"messages\": [\n" +
                "    {\n" +
                "      \"type\": \"message\",\n" +
                "      \"user\": \"U0LNZQBPY\",\n" +
                "      \"text\": \"<@U9FF4RE8G> The story name\",\n" +
                "      \"ts\":" + currentTimestamp +
                "    }\n" +
                "  ],\n" +
                "  \"has_more\": true,\n" +
                "  \"unread_count_display\": 97\n" +
                "}";
    }

    public static String getMockChannelsHistoryResponseWithoutUserHandle(String currentTimestamp) {
        return "{\n" +
                "  \"ok\": true,\n" +
                "  \"messages\": [\n" +
                "    {\n" +
                "      \"type\": \"message\",\n" +
                "      \"user\": \"U0LNZQBPY\",\n" +
                "      \"text\": \"Another unrelated message\",\n" +
                "      \"ts\":" + currentTimestamp +
                "    }\n" +
                "  ],\n" +
                "  \"has_more\": true,\n" +
                "  \"unread_count_display\": 97\n" +
                "}";
    }


    public static String getMockChannelsHistoryResponseMultipleMessage(String... currentTimestamps) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode node = objectMapper.createArrayNode();

        for (int i = 0; i < currentTimestamps.length; i++) {
            if (i % 2 == 0) {
                node.add(objectMapper.readTree(buildMockChannelsHistoryMessage(currentTimestamps[i], "")));
            } else {
                node.add(objectMapper.readTree(buildMockChannelsHistoryMessage(currentTimestamps[i], "<@U9FF4RE8G> ")));
            }
        }

        return "{\n" +
                "  \"ok\": true,\n" +
                "  \"messages\": " + node.toString() + ",\n" +
                "  \"has_more\": true,\n" +
                "  \"unread_count_display\": 97\n" +
                "}";
    }

    private static String buildMockChannelsHistoryMessage(String currentTimestamp, String username) {
        return "{\n" +
                "      \"type\": \"message\",\n" +
                "      \"user\": \"U0LNZQBPY\",\n" +
                "      \"text\": \"" + username + "Another unrelated message\",\n" +
                "      \"ts\":" + currentTimestamp +
                "    }";
    }

    public static String getMockChannelsInfoResponse(String lastReadTimestamp) {
        return "{" +
                "\"ok\":true," +
                "\"channel\":" +
                "{" +
                "\"id\":\"ABCDEFG\"," +
                "\"name\":\"some-channel\"," +
                "\"is_channel\":true," +
                "\"created\":1516384917," +
                "\"is_archived\":false," +
                "\"is_general\":false," +
                "\"unlinked\":0," +
                "\"creator\":\"U89G8PC72\"," +
                "\"name_normalized\":\"quell-platformrequest\"," +
                "\"is_shared\":false," +
                "\"is_org_shared\":false," +
                "\"is_member\":true," +
                "\"is_private\":false," +
                "\"is_mpim\":false," +
                "\"last_read\":" + lastReadTimestamp + "," +
                "\"latest\": {" +
                "\"user\":\"U9FF4RE8G\"," +
                "\"inviter\":\"U0LNZQBPY\"," +
                "\"text\":\"<@U9FF4RE8G> has joined the channel\"," +
                "\"type\":\"message\"," +
                "\"subtype\":\"channel_join\"," +
                "\"ts\":\"1521028713.000228\"" +
                "}," +
                "\"unread_count\":99," +
                "\"unread_count_display\":97," +
                "\"members\":[\"U0EL04E78\",\"U0K340VBR\"]," +
                "\"topic\":{\"value\":\"\",\"creator\":\"\",\"last_set\":0}," +
                "\"purpose\":{" +
                "\"value\":\"some channel description\"," +
                "\"creator\":\"U89G8PC72\"," +
                "\"last_set\":1517432101" +
                "}," +
                "\"previous_names\":[]}}\n";
    }

    @SuppressWarnings("unchecked")
    public static Object deserialize(String dataToDeserialize, Class type) throws IOException {
        return new ObjectMapper().readValue(dataToDeserialize, type);
    }

    public static String getCurrentTimestamp() {
        return "999999999.123456";
    }
}
