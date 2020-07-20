package com.ksyim.hellocron.server.service;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import com.ksyim.hellocron.server.command.AbstractCommand;
import com.ksyim.hellocron.server.command.Command;
import com.ksyim.hellocron.server.command.ScheduleCommand;
import com.ksyim.hellocron.server.cron.CronScheduler;
import com.ksyim.hellocron.util.CaseUtils;
import com.linecorp.armeria.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommandHandler {

    private CronScheduler scheduler;
    private WebClient webClient;

    private static Map<String, Class<?>>commandClassMapping =
            findCommandBeans()
                    .stream()
                    .collect(Collectors.toMap(
                            bd -> parseCommandName(bd.getBeanClassName()),
                            bd -> {
                                try {
                                    String beanClassName = bd.getBeanClassName();
                                    Class<?> clazz = Class.forName(beanClassName);
                                    if (clazz.getSuperclass() == AbstractCommand.class) { return clazz; }
                                    else {
                                        throw new RuntimeException(
                                                String.format(
                                                        "class doesn't extend `%s`",
                                                        AbstractCommand.class.getCanonicalName()));
                                    }
                                }
                                catch (Exception e) { throw new RuntimeException(e); } }));

    public void parse(String input) {
        CommandParser parser = getCommandParser(input);
        String commandName = parser.getParsedCommand();

        if (commandName == "schedule") {
            ScheduleCommand command = parser.getObject(ScheduleCommand.class);

            if (command.cron != null) {
                scheduler.register(command.eventName, command.cron, () -> {
                    webClient.get("http://localhost:3000/api"); });
            }
        }
    }

    private static JCommander getCommander() {
        JCommander.Builder jcBuilder = JCommander.newBuilder();

        for (Map.Entry<String, Class<?>> entry : getCommandClassMapping().entrySet()) {
            String command = entry.getKey();
            Class<?> clazz = entry.getValue();

            Object instance;
            try { instance = clazz.getConstructor().newInstance(); }
            catch (Exception e) { throw new RuntimeException(e); }

            if (!clazz.isAnnotationPresent(Parameters.class)) { jcBuilder.addObject(instance); }
            else { jcBuilder.addCommand(command, instance); }
        }

        return jcBuilder.build();
    }

    private static Map<String, Class<?>> getCommandClassMapping() {
        return commandClassMapping;
    }

    public static CommandParser getCommandParser() {
        return new CommandParser(getCommander());
    }

    public static CommandParser getCommandParser(String input) {
        return new CommandParser(getCommander(), input);
    }

    private static Set<BeanDefinition> findCommandBeans() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(new AnnotationTypeFilter(Command.class));

        return scanner.findCandidateComponents("com.ksyim.hellocron.server.command");
    }

    private static String parseCommandName(String beanClassName) {
        if (!beanClassName.endsWith("Command")) throw new RuntimeException("class name must ends with `Command`.");

        String[] names = beanClassName.split("\\.");
        String className = names[names.length - 1];

        return CaseUtils.pascalToKebab(className).replaceFirst("-command$", "");
    }

    public static class CommandParser {

        private JCommander jc;

        public CommandParser(JCommander jc) {
            this.jc = jc;
        }

        public CommandParser(JCommander jc, String input) {
            this.jc = jc;
            parse(input);
        }

        public void parse(String input) {
            jc.parse(input.split(" "));
        }

        public <T extends AbstractCommand> T getObject(Class<T> clazz) {
            JCommander subJc = jc.getCommands().get(parseCommandName(clazz.getCanonicalName()));

            if (subJc == null) { return clazz.cast(jc.getObjects().get(0)); }
            else { return clazz.cast(subJc.getObjects().get(0)); }
        }

        public String getParsedCommand() {
            return jc.getParsedCommand();
        }
    }
}
