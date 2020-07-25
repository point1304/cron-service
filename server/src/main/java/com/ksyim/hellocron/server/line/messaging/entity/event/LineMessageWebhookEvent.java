package com.ksyim.hellocron.server.line.messaging.entity.event;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.ksyim.hellocron.server.line.messaging.entity.event.message.LineMessage;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonTypeName(LineWebhookEvent.MESSAGE_TYPE_NAME)
@JsonDeserialize(builder = LineMessageWebhookEvent.LineMessageWebhookEventBuilder.class)
public class LineMessageWebhookEvent implements LineWebhookEvent{

    String replyToken;
    String type;
    String mode;
    Long timestamp;
    EventSource source;
    LineMessage message;

    @JsonPOJOBuilder(withPrefix = "")
    public static class LineMessageWebhookEventBuilder {}
}
