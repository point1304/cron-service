package com.ksyim.hellocron.server.line.messaging.command.exception;

import java.util.Arrays;

public class IllegalCommandOptionException extends RuntimeException {

    public IllegalCommandOptionException(String argument) {
        super(String.format("option is invalid invalid : %s", argument));
    }
}
