package com.ksyim.hellocron.server.line.messaging.entity.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class EventSourceTest {

    static ObjectMapper mapper = new ObjectMapper();

    public static <T> T toObject(String data, Class<T> clazz) throws Exception {
        return mapper.readValue(data, new TypeReference<T>() {});
    }

    @Test
    public void testEventSourcePolymorphicBehavior() throws Exception {
        // Serialize
        String USER_ID = "xncvasdjfiqwoe123";
        String GROUP_ID = "groupId###";
        String ROOM_ID = "roomId!@#!@*$)";

        EventSource userEventSource = UserEventSource.builder().userId(USER_ID).build();
        String userEventSourceJson = mapper.writeValueAsString(userEventSource);

        EventSource groupEventSource = GroupEventSource.builder().userId(USER_ID).groupId(GROUP_ID).build();
        String groupEventSourceJson = mapper.writeValueAsString(groupEventSource);

        EventSource roomEventSource = RoomEventSource.builder().userId(USER_ID).roomId(ROOM_ID).build();
        String roomEventSourceJson = mapper.writeValueAsString(roomEventSource);

        // Serialized json shouldn't have `type` property.
        assertEquals(userEventSourceJson, String.format("{\"userId\":\"%s\"}", USER_ID));
        assertEquals(groupEventSourceJson, String.format("{\"userId\":\"%s\",\"groupId\":\"%s\"}", USER_ID, GROUP_ID));
        assertEquals(roomEventSourceJson, String.format("{\"roomId\":\"%s\",\"userId\":\"%s\"}", ROOM_ID, USER_ID));

        // Polymorphic deserialization.
        assertEquals(userEventSource.getClass(), UserEventSource.class);
        assertEquals(groupEventSource.getClass(), GroupEventSource.class);
        assertEquals(roomEventSource.getClass(), RoomEventSource.class);
    }
}
