package com.ksyim.hellocron.server.line.messaging.entity.event.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonSubTypes({
        @JsonSubTypes.Type(value = LineTextMessage.class, name = LineMessage.TEXT_TYPE_NAME)
})
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = true
)
public interface LineMessage {

    String TEXT_TYPE_NAME = "text";
    String IMAGE_TYPE_NAME = "image";
    String VIDEO_TYPE_NAME = "video";
    String AUDIO_TYPE_NAME = "audio";
}
