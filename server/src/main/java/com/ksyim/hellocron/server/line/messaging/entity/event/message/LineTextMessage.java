package com.ksyim.hellocron.server.line.messaging.entity.event.message;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonTypeName(LineMessage.TEXT_TYPE_NAME)
@JsonDeserialize(builder = LineTextMessage.LineTextMessageBuilder.class)
public class LineTextMessage implements LineMessage {

    String id;
    String type;
    String text;

    @JsonPOJOBuilder(withPrefix = "")
    public static class LineTextMessageBuilder {}
}
