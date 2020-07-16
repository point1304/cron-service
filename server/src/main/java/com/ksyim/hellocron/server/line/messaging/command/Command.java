package com.ksyim.hellocron.server.line.messaging.command;

import com.ksyim.hellocron.server.line.messaging.command.exception.IllegalCommandException;

import java.util.Map;

public interface Command {

    void execute();

    Map<String, Class<? extends Command>> commandMapping = Map.ofEntries(
            Map.entry("register-cron", RegisterCronCommand.class),
            Map.entry("cancel-cron", CancelCronCommand.class)
    );

    static CommandBuilder builder() {
        return new CommandBuilder();
    }

    class CommandBuilder {

        private Class<? extends Command> clazz;
        private Map<String, Object> options;

        public CommandBuilder command(String command) {
            this.clazz = commandMapping.get(command);
            if (this.clazz == null) {
                throw new IllegalCommandException(String.format("No such command: %s", command));
            }
            return this;
        }

        public CommandBuilder option(String optionName, boolean optionValue) {
            options.put(optionName, optionValue);
            return this;
        }

        public CommandBuilder option(String optionName, String optionValue) {
            options.put(optionName, optionValue);
            return this;
        }

        public CommandBuilder option(String optionName, String[] optionValues) {
            options.put(optionName, optionValues);
            return this;
        }

        public Command build() {
            try {
                if (options == null) {
                    return clazz.getConstructor().newInstance();
                }
                return clazz.getConstructor(Map.class).newInstance(options);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
