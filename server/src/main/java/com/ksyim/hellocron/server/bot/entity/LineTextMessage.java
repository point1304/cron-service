package com.ksyim.hellocron.server.bot.entity;

import lombok.Value;

@Value
public class LineTextMessage {

    private String type = "text";
    private String text;
}
