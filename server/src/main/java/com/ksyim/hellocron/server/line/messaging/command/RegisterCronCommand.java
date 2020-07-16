package com.ksyim.hellocron.server.line.messaging.command;

import com.ksyim.hellocron.server.line.messaging.command.exception.IllegalCommandOptionException;

import java.util.Map;

public class RegisterCronCommand implements Command {

    private Map<String, Object> options;

    public RegisterCronCommand(Map<String, Object> options) {
        verifyOptions(options);
        this.options = options;
    }

    @Override
    public void execute() {

    }

    static void verifyOptions(Map<String, Object> options) {
    }
}
