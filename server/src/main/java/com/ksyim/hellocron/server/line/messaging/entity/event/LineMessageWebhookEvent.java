package com.ksyim.hellocron.server.line.messaging.entity.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@Builder
@JsonTypeName(LineWebhookEvent.MESSAGE_TYPE_NAME)
@JsonDeserialize(builder = LineMessageWebhookEvent.LineMessageWebhookEventBuilder.class)
public class LineMessageWebhookEvent implements LineWebhookEvent{

    String type;
    String mode;
    Long timestamp;

    @JsonPOJOBuilder(withPrefix = "")
    public static class LineMessageWebhookEventBuilder {}
}
