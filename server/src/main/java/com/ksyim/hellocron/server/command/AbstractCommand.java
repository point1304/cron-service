package com.ksyim.hellocron.server.command;

import com.beust.jcommander.Parameter;

public abstract class AbstractCommand implements Command {

    @Parameter(names = { "-v", "--verbose" }, description = "display logs")
    public boolean verbose;

    @Parameter(names = { "--help", "-h" }, description = "display help messages")
    public boolean help;
}
