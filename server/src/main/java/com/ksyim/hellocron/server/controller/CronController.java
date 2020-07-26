package com.ksyim.hellocron.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ksyim.hellocron.server.bot.context.LineWebhookContext;
import com.ksyim.hellocron.server.bot.service.LineBotService;
import com.ksyim.hellocron.server.command.service.CommandHandlerService;
import com.ksyim.hellocron.server.command.service.InvalidCommandException;
import com.ksyim.hellocron.server.cron.CronScheduler;
import com.ksyim.hellocron.server.line.messaging.entity.event.LineMessageWebhookEvent;
import com.ksyim.hellocron.server.line.messaging.entity.event.LineWebhookEvent;
import com.ksyim.hellocron.server.line.messaging.entity.event.LineWebhookEvents;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Post;
import com.linecorp.armeria.server.annotation.ProducesJson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CronController {

    private static final String COMMAND_PREFIX = "@bot ";

    private final WebClient webClient;
    private final CronScheduler cronScheduler;
    private final CommandHandlerService lineCommandHandler;
    private final LineBotService lineBotService;

    @Get("/")
    public String root() { return "Hi there! It's cron service!"; }

    @Post("/echo")
    @ProducesJson
    public String echo(LineWebhookEvents webhook) {
        String replyToken = webhook.getEvents().get(0).getReplyToken();
        log.info("-:-:LINE_WEBHOOK_EVENT:-:-:replyToken <{}>", replyToken);
        lineBotService.sendReply("경수는 은채를 사랑해요!", replyToken);
        //lineBotService.sendMessage(webhook.toString(), "U92ee079c046d04c6fdc83063ce2571dc");
        return "OK";
    }

    @Post("/webhook")
    @ProducesJson
    public String lineWebhook(LineWebhookEvents webhook) {
        for (LineWebhookEvent event : webhook.getEvents()) {
            // TODO: make getEvents() returns List<@NotNull LineWebhookEvent>
            if (event == null) continue;
            if (!(event instanceof LineMessageWebhookEvent)) continue;

            String text, replyToken, to;

            to = event.getSource().getTo();
            replyToken = event.getReplyToken();
            text = ((LineMessageWebhookEvent) event).getMessage().getText();

            if (!text.startsWith(COMMAND_PREFIX)) {
                String command = text.substring(COMMAND_PREFIX.length());
                try {
                    lineCommandHandler.handleCommand(command, new LineWebhookContext(to));
                } catch (InvalidCommandException e) {
                    lineBotService.sendReply(e.getUsage(), replyToken);
                }
            }
        }
        return "OK";
    }
}
