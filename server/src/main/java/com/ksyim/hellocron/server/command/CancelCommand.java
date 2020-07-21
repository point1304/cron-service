package com.ksyim.hellocron.server.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.ArrayList;
import java.util.List;

@Command
@Parameters(separators = "=", commandDescription = "Cancel a task with given task name")
public class CancelCommand extends AbstractCommand {

    @Parameter(description = "[task name]")
    public String taskName;
}
