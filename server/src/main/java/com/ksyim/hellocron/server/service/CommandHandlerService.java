package com.ksyim.hellocron.server.service;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import com.ksyim.hellocron.server.command.AbstractCommand;
import com.ksyim.hellocron.server.command.Command;
import com.ksyim.hellocron.server.command.ScheduleCommand;
import com.ksyim.hellocron.server.cron.CronScheduler;
import com.ksyim.hellocron.server.entity.response.ApiResponse;
import com.ksyim.hellocron.server.entity.response.ErrorResponse;
import com.ksyim.hellocron.server.entity.response.SuccessResponse;
import com.ksyim.hellocron.server.line.messaging.command.exception.IllegalCommandException;
import com.ksyim.hellocron.server.line.messaging.command.exception.IllegalCommandOptionException;
import com.ksyim.hellocron.util.CaseUtils;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.internal.shaded.caffeine.cache.Scheduler;
import com.sun.net.httpserver.Authenticator;
import io.netty.channel.EventLoopGroup;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

@Slf4j
@Service
@AllArgsConstructor
public class CommandHandlerService {

    private final Validator validator;
    private final CronScheduler cronScheduler;

    private static Map<String, Class<?>>commandClassMapping =
            findCommandBeans()
                    .stream()
                    .collect(Collectors.toMap(
                            // `bd` is short for `BeanDefinition`.
                            bd -> parseCommandNameFromClassPath(bd.getBeanClassName()),
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

    public void handleCommand(String input) {
        CommandParser parser = parseCommand(input);

        ScheduleCommand command = parser.getObject(ScheduleCommand.class);
        validateCommand(command);

    }

    private void validateCommand(AbstractCommand command) {
        Set<ConstraintViolation<AbstractCommand>> violations = validator.validate(command);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private static JCommander getCommander() {
        JCommander.Builder jcBuilder = JCommander.newBuilder();

        for (Map.Entry<String, Class<?>> entry : commandClassMapping.entrySet()) {
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

    public CommandParser parseCommand(String input) {
        return new CommandParser(getCommander(), input);
    }

    private static Set<BeanDefinition> findCommandBeans() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(new AnnotationTypeFilter(Command.class));

        return scanner.findCandidateComponents("com.ksyim.hellocron.server.command");
    }

    private static String parseCommandNameFromClassPath(String beanClassName) {
        if (!beanClassName.endsWith("Command")) throw new RuntimeException("class name must ends with `Command`.");

        String[] names = beanClassName.split("\\.");
        String className = names[names.length - 1];

        return CaseUtils.pascalToKebab(className).replaceFirst("-command$", "");
    }

    // TODO: [Refactoring] CommandHandler's role should remain at parsing and validation. Task scheduling got to be handled in the `Service`.
    public static class CommandParser {

        private final JCommander jc;

        public CommandParser(JCommander jc, String input) {
            this.jc = jc;
            parseAndValidate(input);
        }

        private void parseAndValidate(String input) {
            jc.parse(input.split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)"));
            String commandName = jc.getParsedCommand();

            ScheduleCommand command = getObject(ScheduleCommand.class);
        }

        public <T extends AbstractCommand> T getObject(Class<T> clazz) {
            JCommander subJc = jc.getCommands().get(
                    parseCommandNameFromClassPath(clazz.getCanonicalName()));

            if (subJc == null) { return clazz.cast(jc.getObjects().get(0)); }
            else { return clazz.cast(subJc.getObjects().get(0)); }
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

    public static class Foo {

        @Pattern(regexp = "[0-9]*")
        public String name;

        public Foo(String name) { this.name = name; }
    }
}
