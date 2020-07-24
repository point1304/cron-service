package com.ksyim.hellocron.server.bot.service;

public interface BotService {

    void sendMessage(String message, String identityToken);

    void replyMessage(String message, String replyToken);
}
