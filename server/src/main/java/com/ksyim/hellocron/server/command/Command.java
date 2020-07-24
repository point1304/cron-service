package com.ksyim.hellocron.server.command;

import com.ksyim.hellocron.server.bot.context.WebhookContext;
import com.ksyim.hellocron.server.bot.service.BotService;
import com.ksyim.hellocron.server.cron.CronScheduler;

public interface Command {

    void execute(BotService service, CronScheduler cronScheduler, WebhookContext ctx);
}
