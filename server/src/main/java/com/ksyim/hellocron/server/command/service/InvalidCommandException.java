package com.ksyim.hellocron.server.command.service;

import com.beust.jcommander.JCommander;

public class InvalidCommandException extends Exception {

    private JCommander jc;

    public InvalidCommandException(JCommander jc) {
        this.jc = jc;
    }

    public InvalidCommandException(String message, JCommander jc) {
        super(message);
        this.jc = jc;
    }

    public InvalidCommandException(Throwable cause, JCommander jc) {
        super(cause);
        this.jc = jc;
    }

    public InvalidCommandException(String message, Throwable cause, JCommander jc) {
        super(message, cause);
        this.jc = jc;
    }

    public String getUsage(String command) {
        StringBuilder sb = new StringBuilder();
        jc.getUsageFormatter().usage(command, sb);

        return sb.toString();
    }

    public String getUsage() {
        StringBuilder sb = new StringBuilder();
        jc.getUsageFormatter().usage(sb);

        return sb.toString();
    }
}
