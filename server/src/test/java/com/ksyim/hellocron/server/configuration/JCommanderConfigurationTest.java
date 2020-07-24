package com.ksyim.hellocron.server.configuration;

import com.beust.jcommander.JCommander;
import com.ksyim.hellocron.server.bot.context.LineWebhookContext;
import com.ksyim.hellocron.server.command.ScheduleCommand;
import com.ksyim.hellocron.server.command.service.LineCommandHandlerService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class JCommanderConfigurationTest {

    @Autowired
    private LineCommandHandlerService service;

    @Test
    public void test__commandHandler() {
        service.handleCommand("schedule --cron \"* * * * * *\" helloworld", new LineWebhookContext("sjdklf"));
    }
}
