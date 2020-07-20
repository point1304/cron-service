package com.ksyim.hellocron.server.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.ArrayList;
import java.util.List;

@Command
@Parameters(separators = "=", commandDescription = "Register a cron-job with a cron expression.")
public class RegisterCronCommand extends AbstractCommand {

    @Parameter
    public List<String> params = new ArrayList<>();
}