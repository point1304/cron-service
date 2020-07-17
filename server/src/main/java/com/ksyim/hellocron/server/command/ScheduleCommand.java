package com.ksyim.hellocron.server.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Command
@Parameters(separators = "=", commandDescription = "Schedule a cron task or one-time task")
public class ScheduleCommand {

    @Parameter(names = "--cron", description = "schedule a cron task (can't be used with `--at` option)")
    public String cron;

    @Parameter(names = "--at", description = "schedule a task at a specified time (can't be used with `--cron` option")
    public String at;
}
