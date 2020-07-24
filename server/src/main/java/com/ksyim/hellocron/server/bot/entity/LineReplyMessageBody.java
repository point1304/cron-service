package com.ksyim.hellocron.server.bot.entity;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LineReplyMessageBody {

    private String replyToken;
    private List<LineTextMessage> messages;
}
