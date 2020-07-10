package com.ksyim.hellocron.server.controller;

import com.ksyim.hellocron.server.cron.CronScheduler;
import com.ksyim.hellocron.server.entity.User;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.server.annotation.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@AllArgsConstructor
public class CronController {
    private final WebClient webClient;
    private final CronScheduler cronScheduler;

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

    @Get("/cancel/{eventName}")
    public String schedule(@Param String eventName) {
        return "OK";
    }

    @Get("/user")
    @ProducesJson
    public Mono<User> getUser() {
        return Mono.just(new User("ksyim", 10));
    }

    @Post("/post")
    @ProducesJson
    public Mono<User> postUser(@RequestObject User user) {
        return Mono.just(user);
    }
}
