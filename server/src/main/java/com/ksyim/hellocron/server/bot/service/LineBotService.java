package com.ksyim.hellocron.server.bot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksyim.hellocron.server.bot.entity.LinePushMessageBody;
import com.ksyim.hellocron.server.bot.entity.LineReplyMessageBody;
import com.ksyim.hellocron.server.bot.entity.LineTextMessage;

import com.linecorp.armeria.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LineBotService implements BotService {

    private final WebClient lineMessagingClient;
    private final ObjectMapper mapper;

    @Override
    public void sendMessage(String message,
                            String identityToken) {

        LinePushMessageBody pushMessage =
                LinePushMessageBody.builder()
                                   .to(identityToken)
                                   .messages(List.of(new LineTextMessage(message)))
                                   .build();

        try {
            String body = mapper.writeValueAsString(pushMessage);
            log.info("-:-:PUSH_MESSAGE:-:-:body <{}>", body);
            lineMessagingClient.post("/v2/bot/message/push", body)
                               .aggregate()
                               .thenAccept(res -> log.info("-:-:PUSH_MESSAGE:-:-:response <{}>",
                                                           res.contentUtf8()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendReply(String message,
                          String replyToken) {

        LineReplyMessageBody replyMessage =
                LineReplyMessageBody.builder()
                                    .replyToken(replyToken)
                                    .messages(List.of(new LineTextMessage(message)))
                                    .build();

        try {
            String body = mapper.writeValueAsString(replyMessage);
            log.info("-:-:REPLY_MESSAGE:-:-:body <{}>", body);
            lineMessagingClient.post("/v2/bot/message/reply", body)
                               .aggregate()
                               .thenAccept(res -> log.info("-:-:REPLY_MESSAGE:-:-:response <{}>",
                                                           res.contentUtf8()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
