package com.ksyim.hellocron.server.bot.context;

import lombok.Value;

@Value
public class LineWebhookContext implements WebhookContext {

    private final String sourceIdToken;
}
