package com.ksyim.hellocron.server.line.messaging.entity.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonTypeName(EventSource.USER_TYPE_NAME)
@JsonDeserialize(builder = UserEventSource.UserEventSourceBuilder.class)
public class UserEventSource implements EventSource {

    String userId;

    @Override
    public String getTo() {
        return userId;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class UserEventSourceBuilder {}
}
