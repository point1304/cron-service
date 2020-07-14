package com.ksyim.hellocron.server.line.messaging.entity.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LineMessageWebhookEventTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testLineMessageWebhookEventGetter() throws Exception {
        String TYPE = "message";
        String MODE = "hello, world!";
        Long TIMESTAMP = 129039190L;

        String JSON_DATA = String.format("{" +
                "\"type\": \"%s\"," +
                "\"mode\": \"%s\"," +
                "\"timestamp\": %s" +
                "}", TYPE, MODE, TIMESTAMP);

        LineWebhookEvent webhookEvent = mapper.readValue(JSON_DATA, LineWebhookEvent.class);

        Assertions.assertEquals(webhookEvent.getClass(), LineMessageWebhookEvent.class);
        Assertions.assertEquals(webhookEvent,
                                LineMessageWebhookEvent.builder()
                                                        .type(TYPE)
                                                        .mode(MODE)
                                                        .timestamp(TIMESTAMP)
                                                        .build());
    }
}
