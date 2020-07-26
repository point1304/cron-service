package com.ksyim.hellocron.server.command.service;

import com.ksyim.hellocron.server.bot.context.WebhookContext;

public interface CommandHandlerService {
    void handleCommand(String input, WebhookContext ctx) throws InvalidCommandException;
}
