package com.ksyim.hellocron.server.line.messaging.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksyim.hellocron.server.line.messaging.command.Command;
import com.ksyim.hellocron.server.line.messaging.entity.event.LineMessageWebhookEvent;
import com.linecorp.armeria.common.AggregatedHttpRequest;

import java.util.*;

public class LineMessagingUtils {

    static ObjectMapper mapper = new ObjectMapper()
                                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    static TypeReference<List<LineMessageWebhookEvent>> typeRef = new TypeReference<List<LineMessageWebhookEvent>>() {};

    static AggregatedHttpRequest verifyRequest(AggregatedHttpRequest req) {
        return req;
    }

    static List<LineMessageWebhookEvent> parseEvents(AggregatedHttpRequest req) throws Exception {
        return mapper.readValue(req.content().toStringUtf8(), typeRef);
    }

    static Command parseCommandType(String text) {
        String[] token = text.split(" ");
        String commandType = token[0];
        String[] arguments = Arrays.copyOfRange(token, 1, token.length);

        return Command.builder()
                    .command(commandType)
                    .option("", "")
                    .option("", "")
                    .build();
    }
}
