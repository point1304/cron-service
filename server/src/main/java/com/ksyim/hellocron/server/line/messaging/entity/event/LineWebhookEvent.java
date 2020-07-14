package com.ksyim.hellocron.server.line.messaging.entity.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonSubTypes({
        @JsonSubTypes.Type(value = LineMessageWebhookEvent.class, name = "message")
})
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = true
)
interface LineWebhookEvent {}
