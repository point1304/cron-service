package com.ksyim.hellocron.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ksyim.hellocron.server.bot.context.LineWebhookContext;
import com.ksyim.hellocron.server.command.service.CommandHandlerService;
import com.ksyim.hellocron.server.cron.CronScheduler;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Param;
import com.linecorp.armeria.server.annotation.Post;
import com.linecorp.armeria.server.annotation.ProducesJson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CronController {
    private final WebClient webClient;
    private final CronScheduler cronScheduler;
    private final CommandHandlerService lineCommandHandler;

    @Get("/")
    public String root() { return "Hi there! It's cron service!"; }

    @Get("/schedule/{eventName}/{cronExp}/{endpoint}")
    @ProducesJson
    public String schedule(@Param String eventName,
                           @Param String cronExp,
                           @Param String endpoint) {
        cronScheduler.register(eventName, cronExp, () -> webClient.get("/api").aggregate().thenAccept(res -> {
            System.out.println(res.content().toStringUtf8());
        }));
        return "OK";
    }

    @Get("/messaging")
    @ProducesJson
    public String handleMessagingApiWebHook() {
        lineCommandHandler.handleCommand("schedule --cron \"* * * * * *\" --message hello-world eventName",
                                         new LineWebhookContext("asdfasdfjljakdsf"));
        return "OK";
    }

    @Post("/test")
    @ProducesJson
    public String handleTest(ServiceRequestContext ctx, AggregatedHttpRequest req) {
        return "OK";
    }
}
