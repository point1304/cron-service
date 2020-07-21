package com.ksyim.hellocron.server.command;

import javax.validation.constraints.Pattern;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.ksyim.hellocron.server.command.validation.CronExpression;
import com.ksyim.hellocron.server.command.validation.OnlyOneAmong;
import com.ksyim.hellocron.server.cron.CronScheduler;

import com.linecorp.armeria.client.WebClient;

@OnlyOneAmong(properties = { "cron", "at" }, message = "{properties} options are incompatible")
@Command
@Parameters(separators = "=", commandDescription = "Schedule a cron task or one-time task")
public class ScheduleCommand extends AbstractCommand {

    @CronExpression
    @Parameter(names = "--cron", description = "schedule a cron task (can't be used with `--at`)")
    public String cron;

    @Parameter(names = "--at",
            description = "schedule a task at a specified time (can't be used with `--cron`)")
    public String at;

    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "Only ascii characters are allowed. The length must be gte 4 and lte 20.")
    @Parameter(description = "[task name]")
    public String eventName;

    @Override
    public void execute(WebClient client, CronScheduler cronScheduler) {

    }
}