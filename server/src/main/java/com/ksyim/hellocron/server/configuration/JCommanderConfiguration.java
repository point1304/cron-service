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

@Configuration
public class JCommanderConfiguration {

    @Bean
    public JCommander getLineCommander() throws Exception {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(new AnnotationTypeFilter(Command.class));

        JCommander.Builder jcBuilder = JCommander.newBuilder();

        for (BeanDefinition bd : scanner.findCandidateComponents("com.ksyim.hellocron.server.command")) {
            Class<?> clazz = Class.forName(bd.getBeanClassName());
            String commandName = parseCommandName(clazz);
            Object instance = clazz.getConstructor().newInstance();

            if (clazz.isAnnotationPresent(Parameters.class)) jcBuilder.addObject(instance);
            else jcBuilder.addCommand(commandName, instance);
        }

        return jcBuilder.build();
    }

    static String parseCommandName(Class<?> clazz) {
        String className = clazz.getSimpleName();
        if (!className.endsWith("Command")) throw new RuntimeException("CommandClass must ends with `Command`");

        return CaseUtils.pascalToKebab(clazz.getSimpleName()).replaceFirst("-command$", "");
    }
}
