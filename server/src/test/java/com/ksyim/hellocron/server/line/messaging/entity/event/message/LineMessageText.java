package com.ksyim.hellocron.server.line.messaging.entity.event.message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LineMessageText {

    private static ObjectMapper mapper = new ObjectMapper()
                                                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                                                        false);

    private static <T> T toObject(String jsonData, Class<T> clazz) throws Exception {
        return mapper.readValue(jsonData, new TypeReference<T>() {});
    }

    @Test
    public void testLineMessageModelPolymorphicBehavior() throws Exception {
        String REQUEST_BODY = "{" +
                "\"id\":\"point1304\"," +
                "\"type\":\"text\"," +
                "\"text\":\"hello, world!\"," +
                "\"emojis\": [{" +
                    "\"index\":14," +
                    "\"length\":6," +
                    "\"productId\":\"aasdjk1234jklasdf\"," +
                    "\"emojiId\":\"001\"" +
                "}]" +
        "}";

        LineMessage lineMessageObject = toObject(REQUEST_BODY, LineMessage.class);
        System.out.println(lineMessageObject.toString());
    }
}
