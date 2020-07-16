package com.ksyim.hellocron.server.line.messaging.command.exception;

public class IllegalCommandException extends RuntimeException {

    public IllegalCommandException(String message) {
        super(message);
    }
}
