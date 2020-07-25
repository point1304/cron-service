package com.ksyim.hellocron.server.line.messaging.entity.event;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonTypeName(EventSource.ROOM_TYPE_NAME)
@JsonDeserialize(builder = RoomEventSource.RoomEventSourceBuilder.class)
public class RoomEventSource implements EventSource {

    String roomId;
    String userId;

    @Override
    public String getTo() {
        return roomId;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class RoomEventSourceBuilder {}
}
