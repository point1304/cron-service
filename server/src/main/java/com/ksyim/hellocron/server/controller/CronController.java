package com.ksyim.hellocron.server.controller;

import com.ksyim.hellocron.server.entity.User;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.*;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Controller
@AllArgsConstructor
public class CronController {
    private final WebClient webClient;
    private final ThreadPoolTaskScheduler scheduler;
    private final Map<String, ScheduledFuture> cronMap = new HashMap<>();

    @Get("/")
    public HttpResponse root() {
        return webClient.get("/");
    }

    @Get("/schedule/{eventName}/{frequency}")
    @ProducesJson
    public String schedule(@Param String eventName,
                            @Param String frequency) {

        synchronized(this) {
            if (!cronMap.containsKey(eventName)) {
                ScheduledFuture future = scheduler.schedule(
                        () -> { System.out.println("hello world!!!!"); },
                        new CronTrigger("0/5 * * ? * *")
                );
                cronMap.put(eventName, future);
            }
        }

        return "OK";
    }

    @Get("/cancel/{eventName}")
    public String schedule(@Param String eventName) {
        cronMap.remove(eventName).cancel(false);

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
