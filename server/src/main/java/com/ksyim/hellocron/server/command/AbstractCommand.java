package com.ksyim.hellocron.server.command;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public abstract class AbstractCommand {

    @Parameter(names = {"-v", "--verbose"}, description = "display logs")
    public boolean verbose;

    @Parameter(names = {"--help", "-h"}, description = "display help messages")
    public boolean help;
}
