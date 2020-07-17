package com.ksyim.hellocron.server.command;

import com.beust.jcommander.JCommander;
import com.ksyim.hellocron.util.CaseUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class RegisterCronCommandTest {

    @Test
    public void test__options() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(new AnnotationTypeFilter(Command.class));

        JCommander.Builder jcBuilder = JCommander.newBuilder();

        for (BeanDefinition bd : scanner.findCandidateComponents("com.ksyim.hellocron.server.command")) {
            Class<?> clazz;
            try { clazz = Class.forName(bd.getBeanClassName()); }
            catch (ClassNotFoundException e) { throw new RuntimeException(e); }

            String commandName = CaseUtils.pascalToKebab(clazz.getSimpleName()).replaceFirst("-command$", "");

            try { jcBuilder.addCommand(commandName, clazz.getConstructor().newInstance()); }
            catch (Exception e) { throw new RuntimeException(e); }
        }
    }
}
