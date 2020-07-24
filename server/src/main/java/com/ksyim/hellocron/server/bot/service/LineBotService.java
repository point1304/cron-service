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
            lineMessagingClient.post("/v2/bot/message/push",
                                     mapper.writeValueAsString(pushMessage));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void replyMessage(String message,
                             String replyToken) {

        LineReplyMessageBody replyMessage =
                LineReplyMessageBody.builder()
                                    .replyToken(replyToken)
                                    .messages(List.of(new LineTextMessage(message)))
                                    .build();

        try {
            lineMessagingClient.post("/v2/bot/message/reply",
                                     mapper.writeValueAsString(replyMessage));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
