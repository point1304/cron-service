package com.ksyim.hellocron.server.line.messaging.entity.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonSubTypes({
        @JsonSubTypes.Type(value = LineMessageWebhookEvent.class, name = LineWebhookEvent.MESSAGE_TYPE_NAME)
})
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = true,
        defaultImpl = Void.class
)
public interface LineWebhookEvent {
    String MESSAGE_TYPE_NAME = "message";
    String FOLLOW_TYPE_NAME = "follow";
    String UNFOLLOW_TYPE_NAME = "unfollow";
    String JOIN_TYPE_NAME = "join";

    EventSource getSource();
    String getReplyToken();
}
