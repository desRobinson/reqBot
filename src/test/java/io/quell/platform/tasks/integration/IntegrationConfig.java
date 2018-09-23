package io.quell.platform.tasks.integration;

import org.junit.Before;
import org.junit.Rule;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

public class IntegrationConfig {

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, 8089);

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected MockServerClient mockServerClient;

    @Before
    public void setUp() {
        mockServerClient = new MockServerClient("localhost", 8089);
    }
}
