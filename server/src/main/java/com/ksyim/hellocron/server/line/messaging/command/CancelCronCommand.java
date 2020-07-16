package com.ksyim.hellocron.server.line.messaging.command;

import com.ksyim.hellocron.server.line.messaging.command.exception.IllegalCommandOptionException;

public class CancelCronCommand implements Command {

    private String[] args;

    public CancelCronCommand(String[] args) {
        this.args = args;
    }

    @Override
    public void execute() {

    }
}
