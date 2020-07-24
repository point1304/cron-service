package com.ksyim.hellocron.server.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.ksyim.hellocron.server.bot.context.WebhookContext;
import com.ksyim.hellocron.server.bot.service.BotService;
import com.ksyim.hellocron.server.cron.CronScheduler;

//@AsCommand
@Parameters(separators = "=", commandDescription = "Cancel a task with given task name")
public class CancelCommand extends AbstractCommand {

    @Parameter(description = "[task name]")
    public String taskName;

    @Override
    public void execute(BotService service, CronScheduler cronScheduler, WebhookContext ctx) {

    }
}
