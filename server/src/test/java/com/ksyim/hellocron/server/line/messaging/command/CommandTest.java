package com.ksyim.hellocron.server.line.messaging.command;

import com.ksyim.hellocron.server.line.messaging.command.exception.IllegalCommandException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class CommandTest {

    @Test
    public void test__CommandBuilderPolymorphicInstantiation() {
        Command registerCronCommand = Command.builder().command("register-cron").build();
        Command cancelCronCommand = Command.builder().command("cancel-cron").build();

        assertEquals(registerCronCommand.getClass(), RegisterCronCommand.class);
        assertEquals(cancelCronCommand.getClass(), CancelCronCommand.class);
    }

    @Test
    public void test__IllegalCommandException() {
        assertThrows(IllegalCommandException.class, () -> Command.builder().command("hello-world").build());
    }
}
