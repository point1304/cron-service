package com.ksyim.hellocron.server.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.ksyim.hellocron.server.cron.CronScheduler;

import java.util.ArrayList;
import java.util.List;

import com.linecorp.armeria.client.WebClient;

@Command
@Parameters(separators = "=", commandDescription = "Cancel a task with given task name")
public class CancelCommand extends AbstractCommand {

    @Parameter(description = "[task name]")
    public String taskName;

    @Override
    public void execute(WebClient client, CronScheduler cronScheduler) {

    }
}
