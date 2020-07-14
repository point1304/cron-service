package com.ksyim.hellocron.server.line.messaging.entity.event;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonTypeName(EventSource.GROUP_TYPE_NAME)
@JsonDeserialize(builder = GroupEventSource.GroupEventSourceBuilder.class)
public class GroupEventSource implements EventSource {

    String userId;
    String groupId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class GroupEventSourceBuilder {}
}
