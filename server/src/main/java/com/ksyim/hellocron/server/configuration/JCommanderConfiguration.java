package com.ksyim.hellocron.server.configuration;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import com.ksyim.hellocron.server.command.Command;
import com.ksyim.hellocron.util.CaseUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

@Configuration
public class JCommanderConfiguration {

    @Bean
    public JCommander getLineCommander() throws Exception {
        JCommander.Builder jcBuilder = JCommander.newBuilder();

        for (BeanDefinition bd : findCommandBeans()) {
            Class<?> commandClass = Class.forName(bd.getBeanClassName());
            String commandName = parseCommandName(commandClass);
            Object instance = commandClass.getConstructor().newInstance();

            if (!commandClass.isAnnotationPresent(Parameters.class)) jcBuilder.addObject(instance);
            else jcBuilder.addCommand(commandName, instance);
        }

        return jcBuilder.build();
    }

    private static Set<BeanDefinition> findCommandBeans() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(new AnnotationTypeFilter(Command.class));

        return scanner.findCandidateComponents("com.ksyim.hellocron.server.command");
    }

    private static String parseCommandName(Class<?> clazz) {
        String className = clazz.getSimpleName();
        if (!className.endsWith("Command")) throw new RuntimeException("CommandClass must ends with `Command`");

        return CaseUtils.pascalToKebab(clazz.getSimpleName()).replaceFirst("-command$", "");
    }
}
