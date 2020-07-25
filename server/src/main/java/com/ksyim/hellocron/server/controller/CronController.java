package com.ksyim.hellocron.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ksyim.hellocron.server.bot.context.LineWebhookContext;
import com.ksyim.hellocron.server.bot.service.LineBotService;
import com.ksyim.hellocron.server.command.service.CommandHandlerService;
import com.ksyim.hellocron.server.cron.CronScheduler;
import com.ksyim.hellocron.server.line.messaging.entity.event.LineMessageWebhookEvent;
import com.ksyim.hellocron.server.line.messaging.entity.event.LineWebhookEvent;
import com.ksyim.hellocron.server.line.messaging.entity.event.LineWebhookEvents;

import com.linecorp.armeria.client.WebClient;
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
    private final LineBotService lineBotService;

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

    @Get("/echo")
    @ProducesJson
    public String echo(@Param String replyToken) {
        lineBotService.replyMessage("hello, This is CRON-BOT!", replyToken);
        return "OK";
    }

    @Post("/webhook")
    @ProducesJson
    public String lineWebhook(LineWebhookEvents webhook) {
        for (LineWebhookEvent event : webhook.getEvents()) {
            // TODO: make getEvents() returns List<@NotNull LineWebhookEvent>
            if (event == null) continue;

            String text, replyToken, to;

            if (event instanceof LineMessageWebhookEvent) {
                to = event.getSource().getTo();
                replyToken = event.getReplyToken();
                text = ((LineMessageWebhookEvent) event).getMessage().getText();

                if (!text.equals("")) {
                    lineCommandHandler.handleCommand(text,
                                                     new LineWebhookContext(to));
                }
            }
        }
        return "OK";
    }
}
