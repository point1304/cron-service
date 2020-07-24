package com.ksyim.hellocron.server.command.service;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksyim.hellocron.server.bot.service.BotService;
import com.ksyim.hellocron.server.cron.CronScheduler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LineCommandHandlerService extends AbstractCommandHandlerService {

    private final CronScheduler scheduler;
    private final BotService lineBotService;
    private final Validator validator;

    @Override
    protected CronScheduler getCronScheduler() {
        return scheduler;
    }

    @Override
    protected Validator getValidator() {
        return validator;
    }

    @Override
    protected BotService getBotService() {
        return lineBotService;
    }
}
