package com.ksyim.hellocron.server.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.ksyim.hellocron.server.bot.context.WebhookContext;
import com.ksyim.hellocron.server.bot.service.BotService;
import com.ksyim.hellocron.server.command.validation.CronExpression;
import com.ksyim.hellocron.server.command.validation.OnlyOneAmong;
import com.ksyim.hellocron.server.cron.CronScheduler;

import lombok.NonNull;

@AsCommand
@Parameters(separators = "=", commandDescription = "Schedule a cron task or one-time task")
@OnlyOneAmong(properties = { "cron", "at" }, message = "{properties} options are incompatible")
public class ScheduleCommand extends AbstractCommand {

    @CronExpression
    @Parameter(names = "--cron", description = "schedule a cron task (can't be used with `--at`)")
    public String cron;

    @Parameter(names = "--at",
            description = "schedule a task at a specified time (can't be used with `--cron`)")
    public String at;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$",
            message = "Only ascii characters are allowed. The length must be gte 4 and lte 20.")
    @Parameter(description = "[task name]")
    public String eventName;

    @Pattern(regexp = ".{5,300}", message = "message must be longer than 5 and shorter than 300")
    @Parameter(names = { "--message", "-m" },
            description = "Message to send. The number of characters in a message must be within 5 - 300.")
    public String message;

    @Override
    public void execute(@NonNull BotService service,
                        @NonNull CronScheduler cronScheduler,
                        @NonNull WebhookContext ctx) {

        cronScheduler.register(eventName, cron, () -> {
            service.sendMessage(message, ctx.getSourceIdToken());
        });
    }
}