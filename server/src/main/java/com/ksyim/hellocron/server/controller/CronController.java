package com.ksyim.hellocron.server.controller;

import com.ksyim.hellocron.server.entity.User;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CronController {
    @Autowired
    private WebClient webClient;

    @Get("/")
    public HttpResponse root() {
        return webClient.get("/latest/wedCard/love");
    }

    @Get("/schedule/{eventName}/{frequency}")
    @ProducesJson
    public String schedule(@Param String eventName,
                            @Param String frequency) {
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
