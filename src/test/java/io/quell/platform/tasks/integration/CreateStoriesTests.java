package io.quell.platform.tasks.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.quell.platform.tasks.request.PlatformLoad;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static io.quell.platform.tasks.TestData.*;
import static org.mockserver.model.HttpRequest.request;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class CreateStoriesTests extends IntegrationConfig {

    @Autowired
    PlatformLoad platformLoad;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testCreateStoryWithName() throws Exception {

        String expectedRequest = getStoryExpectedRequest();
        String mockResponse = getMockStoryResponseWithName();

        String message = "{\n" +
                "    \"type\": \"app_mention\",\n" +
                "    \"user\": \"user-making-tasks\",\n" +
                "    \"text\": \"<@U9FF4RE8G> The story name\",\n" +
                "    \"ts\": \"timestamp\",\n" +
                "    \"channel\": \"channel-name\",\n" +
                "    \"event_ts\": \"event-timestamp\"\n" +
                "}";

        Header[] mockServerRequestHeaders = new Header[] {
                Header.header("X-TrackerToken", "123456789123456789"),
                Header.header("Content-Type", "application/json")
        };

        HttpRequest mockServerRequest = request()
                .withPath("/service/20202020/call")
                .withMethod("POST")
                .withBody(expectedRequest)
                .withHeaders(mockServerRequestHeaders);

        mockServerClient.when(mockServerRequest)
                .respond(HttpResponse.response(mockResponse)
                        .withStatusCode(HttpStatusCode.OK_200.code()));

        mockMvc.perform(
                post("/stories")
                        .header("Content-Type", "application/json")
                        .accept("application/json")
                        .content(message))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateStoryWithNameAndDescription() throws Exception {

        String expectedRequest = "{\"name\":\"The story name\",\"description\":\"The story description\"}";
        String mockResponse = getMockStoryResponseWithNameAndDescription();

        String message = "{\n" +
                "    \"type\": \"app_mention\",\n" +
                "    \"user\": \"user-making-tasks\",\n" +
                "    \"text\": \"<@U9FF4RE8G> name: The story name description: The story description\",\n" +
                "    \"ts\": \"timestamp\",\n" +
                "    \"channel\": \"channel-name\",\n" +
                "    \"event_ts\": \"event-timestamp\"\n" +
                "}";

        Header[] mockServerRequestHeaders = new Header[] {
                Header.header("X-TrackerToken", "123456789123456789"),
                Header.header("Content-Type", "application/json")
        };

        HttpRequest mockServerRequest = request()
                .withPath("/service/20202020/call")
                .withMethod("POST")
                .withBody(expectedRequest)
                .withHeaders(mockServerRequestHeaders);

        mockServerClient.when(mockServerRequest)
                .respond(HttpResponse.response(mockResponse)
                        .withStatusCode(HttpStatusCode.OK_200.code()));

        mockMvc.perform(
                post("/stories")
                        .header("Content-Type", "application/json")
                        .accept("application/json")
                        .content(message))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateStoryWithNameAndDescriptionUsingMixedCaseTags() throws Exception {

        String expectedRequest = "{\"name\":\"The story name\",\"description\":\"The story description\"}";
        String mockResponse = getMockStoryResponseWithNameAndDescription();

        String message = "{\n" +
                "    \"type\": \"app_mention\",\n" +
                "    \"user\": \"user-making-tasks\",\n" +
                "    \"text\": \"<@U9FF4RE8G> nAmE: The story name deScriPtion: The story description\",\n" +
                "    \"ts\": \"timestamp\",\n" +
                "    \"channel\": \"channel-name\",\n" +
                "    \"event_ts\": \"event-timestamp\"\n" +
                "}";

        Header[] mockServerRequestHeaders = new Header[] {
                Header.header("X-TrackerToken", "123456789123456789"),
                Header.header("Content-Type", "application/json")
        };

        HttpRequest mockServerRequest = request()
                .withPath("/service/20202020/call")
                .withMethod("POST")
                .withBody(expectedRequest)
                .withHeaders(mockServerRequestHeaders);

        mockServerClient.when(mockServerRequest)
                .respond(HttpResponse.response(mockResponse)
                        .withStatusCode(HttpStatusCode.OK_200.code()));

        mockMvc.perform(
                post("/stories")
                        .header("Content-Type", "application/json")
                        .accept("application/json")
                        .content(message))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateStoryWithNoNameTagButTextAndDescription() throws Exception {

        String expectedRequest = "{\"name\":\"random text\",\"description\":\"The story description\"}";
        String mockResponse = getMockStoryResponseWithNameAndDescription();

        String message = "{\n" +
                "    \"type\": \"app_mention\",\n" +
                "    \"user\": \"user-making-tasks\",\n" +
                "    \"text\": \"<@U9FF4RE8G> random text description: The story description\",\n" +
                "    \"ts\": \"timestamp\",\n" +
                "    \"channel\": \"channel-name\",\n" +
                "    \"event_ts\": \"event-timestamp\"\n" +
                "}";

        Header[] mockServerRequestHeaders = new Header[] {
                Header.header("X-TrackerToken", "123456789123456789"),
                Header.header("Content-Type", "application/json")
        };

        HttpRequest mockServerRequest = request()
                .withPath("/service/20202020/call")
                .withMethod("POST")
                .withBody(expectedRequest)
                .withHeaders(mockServerRequestHeaders);

        mockServerClient.when(mockServerRequest)
                .respond(HttpResponse.response(mockResponse)
                        .withStatusCode(HttpStatusCode.OK_200.code()));

        mockMvc.perform(
                post("/stories")
                        .header("Content-Type", "application/json")
                        .accept("application/json")
                        .content(message))
                .andExpect(status().isOk());
    }

    @Test
    public void testPollForRequestThenCreateStory() throws JsonProcessingException {

        String oldestTimestamp  = "123456789.123456";
        String currentTimestamp = "123499999.123456";

        String mockChannelsInfoResponse = getMockChannelsInfoResponse(oldestTimestamp);
        String mockChannelsHistoryResponse = getMockChannelsHistoryResponse(currentTimestamp);

        String expectedChannelsMarkRequest = "{\"channel\":\"testchannel\",\"ts\":\"" + currentTimestamp + "\"}";
        String mockChannelsMarkResponse = getMockChannelsMarkResponse();

        Header[] mockServerRequestHeaders = new Header[] {
                Header.header("Content-Type", "application/json"),
                Header.header("Authorization", "Bearer someToken")
        };

        HttpRequest mockServerChannelInfoRequest = request()
                .withPath("/api/channels.info")
                .withMethod("GET")
                .withQueryStringParameter("channel", "testchannel")
                .withHeaders(mockServerRequestHeaders);

        mockServerClient.when(mockServerChannelInfoRequest)
                .respond(HttpResponse.response(mockChannelsInfoResponse)
                        .withHeader("Content-Type", "application/json")
                        .withStatusCode(HttpStatusCode.OK_200.code()));

        HttpRequest mockServerChannelHistoryRequest = request()
                .withPath("/api/channels.history")
                .withMethod("GET")
                .withQueryStringParameter("channel", "testchannel")
                .withQueryStringParameter("oldest", oldestTimestamp)
                .withHeaders(mockServerRequestHeaders);

        mockServerClient.when(mockServerChannelHistoryRequest)
                .respond(HttpResponse.response(mockChannelsHistoryResponse)
                        .withHeader("Content-Type", "application/json")
                        .withStatusCode(HttpStatusCode.OK_200.code()));

        HttpRequest mockServerChannelMarkRequest = request()
                .withPath("/api/channels.mark")
                .withMethod("POST")
                .withBody(expectedChannelsMarkRequest)
                .withHeaders(mockServerRequestHeaders);

        mockServerClient.when(mockServerChannelMarkRequest)
                .respond(HttpResponse.response(mockChannelsMarkResponse)
                        .withHeader("Content-Type", "application/json")
                        .withStatusCode(HttpStatusCode.OK_200.code()));

        HttpRequest mockServerControllerRequest = request()
                .withPath("/stories")
                .withMethod("POST")
                .withBody("{\"type\":\"message\",\"user\":\"U0LNZQBPY\",\"text\":\"<@U9FF4RE8G> The story name\",\"ts\":\"123499999.123456\"}")
                .withHeaders(new Header("Content-Type", "application/json"));

        mockServerClient.when(mockServerControllerRequest)
                .respond(HttpResponse.response()
                        .withHeader("Content-Type", "application/json")
                        .withStatusCode(HttpStatusCode.OK_200.code()));

        platformLoad.processRequests();

        mockServerClient.verify(mockServerChannelInfoRequest,
                                mockServerChannelHistoryRequest,
                                mockServerControllerRequest,
                                mockServerChannelMarkRequest);
    }

    private static String getStoryExpectedRequest() {
        return "{\"name\":\"The story name\",\"description\":\"The story name\"}";
    }

    private static String getMockStoryResponseWithName() {
        return "{\n" +
                "            \"kind\": \"story\",\n" +
                "            \"id\": 1010101010,\n" +
                "            \"project_id\": 20202020,\n" +
                "            \"name\": \"The story name\",\n" +
                "            \"description\": \"The story name\",\n" +
                "            \"story_type\": \"feature\",\n" +
                "            \"current_state\": \"unscheduled\",\n" +
                "            \"requested_by_id\": 30303030,\n" +
                "            \"owner_ids\": [],\n" +
                "            \"labels\": [],\n" +
                "            \"created_at\": \"2018-03-01T16:58:44Z\",\n" +
                "            \"updated_at\": \"2018-03-01T16:58:44Z\",\n" +
                "            \"url\": \"https://testdomain.com/story/show/1010101010\"\n" +
                "        }";
    }

    private static String getMockStoryResponseWithNameAndDescription() {
        return "{\n" +
                "            \"kind\": \"story\",\n" +
                "            \"id\": 1010101010,\n" +
                "            \"project_id\": 20202020,\n" +
                "            \"name\": \"The story name\",\n" +
                "            \"description\": \"The story description\",\n" +
                "            \"story_type\": \"feature\",\n" +
                "            \"current_state\": \"unscheduled\",\n" +
                "            \"requested_by_id\": 30303030,\n" +
                "            \"owner_ids\": [],\n" +
                "            \"labels\": [],\n" +
                "            \"created_at\": \"2018-03-01T16:58:44Z\",\n" +
                "            \"updated_at\": \"2018-03-01T16:58:44Z\",\n" +
                "            \"url\": \"https://testdomain.com/story/show/1010101010\"\n" +
                "        }";
    }
}
