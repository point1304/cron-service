package com.ksyim.hellocron.server.line.messaging.entity.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonSubTypes({
        @JsonSubTypes.Type(value = UserEventSource.class, name = EventSource.USER_TYPE_NAME),
        @JsonSubTypes.Type(value = GroupEventSource.class, name = EventSource.GROUP_TYPE_NAME),
        @JsonSubTypes.Type(value = RoomEventSource.class, name = EventSource.ROOM_TYPE_NAME)
})
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
public interface EventSource {
    String USER_TYPE_NAME = "user";
    String GROUP_TYPE_NAME = "group";
    String ROOM_TYPE_NAME = "room";

    String getTo();
}
