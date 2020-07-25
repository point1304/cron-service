package com.ksyim.hellocron.server.line.messaging.entity.event;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = LineWebhookEvents.LineWebhookEventsBuilder.class)
public class LineWebhookEvents {

    private String destination;
    private List<LineWebhookEvent> events;

    @JsonPOJOBuilder(withPrefix = "")
    public static class LineWebhookEventsBuilder {}
}
