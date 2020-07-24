package com.ksyim.hellocron.server.command.service;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import com.ksyim.hellocron.server.bot.context.WebhookContext;
import com.ksyim.hellocron.server.bot.service.BotService;
import com.ksyim.hellocron.server.command.AsCommand;
import com.ksyim.hellocron.server.command.Command;
import com.ksyim.hellocron.server.command.ScheduleCommand;
import com.ksyim.hellocron.server.cron.CronScheduler;
import com.ksyim.hellocron.util.CaseUtils;

public abstract class AbstractCommandHandlerService implements CommandHandlerService {

    private static Map<String, Class<?>> commandClassMapping =
            findCommandBeans()
                    .stream()
                    .collect(Collectors.toMap(
                            // `bd` is short for `BeanDefinition`.
                            bd -> parseCommandNameFromClassPath(
                                    Objects.requireNonNull(bd.getBeanClassName(), "bean classname")),
                            bd -> {
                                try {
                                    String beanClassName = bd.getBeanClassName();
                                    Class<?> clazz = Class.forName(beanClassName);
                                    if (Command.class.isAssignableFrom(clazz)) { return clazz; } else {
                                        throw new RuntimeException(
                                                String.format(
                                                        "class doesn't implement `%s`",
                                                        Command.class.getCanonicalName()));
                                    }
                                } catch (Exception e) { throw new RuntimeException(e); }
                            }));

    public static CommandParser parseCommand(String input) {
        return new CommandParser(getCommander(), input);
    }

    private static JCommander getCommander() {
        JCommander.Builder jcBuilder = JCommander.newBuilder();

        for (Map.Entry<String, Class<?>> entry : commandClassMapping.entrySet()) {
            String command = entry.getKey();
            Class<?> clazz = entry.getValue();

            Object instance;
            try { instance = clazz.getConstructor().newInstance(); } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (!clazz.isAnnotationPresent(Parameters.class)) { jcBuilder.addObject(instance); } else {
                jcBuilder.addCommand(command, instance);
            }
        }

        return jcBuilder.build();
    }

    private static Set<BeanDefinition> findCommandBeans() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(new AnnotationTypeFilter(AsCommand.class));

        return scanner.findCandidateComponents("com.ksyim.hellocron.server.command");
    }

    private static String parseCommandNameFromClassPath(String beanClassName) {
        if (!beanClassName.endsWith("Command")) {
            throw new RuntimeException(
                    "class name must ends with `Command`.");
        }

        String[] names = beanClassName.split("\\.");
        String className = names[names.length - 1];

        return CaseUtils.pascalToKebab(className).replaceFirst("-command$", "");
    }

    public void handleCommand(String input, WebhookContext ctx) {
        CommandParser parser = parseCommand(input);

        Command command = parser.getObject(ScheduleCommand.class);
        validateAndExecute(command, ctx);
    }

    private void validateAndExecute(Command command, WebhookContext ctx) {
        Set<ConstraintViolation<Command>> violations = getValidator().validate(command);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        command.execute(getBotService(),
                        getCronScheduler(),
                        ctx);
    }

    abstract protected CronScheduler getCronScheduler();

    abstract protected Validator getValidator();

    abstract protected BotService getBotService();

    public static class CommandParser {

        private final JCommander jc;

        public CommandParser(JCommander jc, String input) {
            this.jc = jc;
            parse(input);
        }

        private void parse(String input) {
            String[] argv = input.split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            argv = Arrays.stream(argv).map(s -> s.replaceAll("\"", "")).toArray(String[]::new);
            jc.parse(argv);
        }

        public <T extends Command> T getObject(Class<T> clazz) {
            JCommander subJc = jc.getCommands().get(
                    parseCommandNameFromClassPath(clazz.getCanonicalName()));

            if (subJc == null) { return clazz.cast(jc.getObjects().get(0)); } else {
                return clazz.cast(subJc.getObjects().get(0));
            }
        }

        public String getParsedCommand() {
            return jc.getParsedCommand();
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
}
